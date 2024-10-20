package com.necklife.api.service.history.v3;

import com.necklife.api.entity.goal.GoalEntity;
import com.necklife.api.entity.history.HistoryEntity;
import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.entity.history.PoseStatus;
import com.necklife.api.entity.history.RawHistoryEntity;
import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.history.HistoryRepository;
import com.necklife.api.repository.history.HistorySummaryRepository;
import com.necklife.api.repository.history.RawHistoryRepository;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.service.goal.GetActiveGoalService;
import com.necklife.api.service.history.SaveRawDataService;
import com.necklife.api.service.streak.UpdateStreakService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SavePitchForwardTiltRawHistoryService {

	private final MemberRepository memberRepository;

	private final HistoryRepository historyRepository;
	private final HistorySummaryRepository historySummaryRepository;
	private final GetActiveGoalService getActiveGoalService;
	private final UpdateStreakService updateStreakService;

	private final SaveRawDataService saveRawDataService;
	private final RawHistoryRepository rawHistoryRepository;

	public void execute(
			String memberId,
			List<TreeMap<LocalDateTime, PoseStatus>> pitch,
			List<TreeMap<LocalDateTime, PoseStatus>> forward,
			List<TreeMap<LocalDateTime, PoseStatus>> tilt,
			List<Map<String, String>> rawData) {
		MemberEntity memberEntity = checkExistMember(memberId);

		List<RawHistoryEntity> forSaveRawHistoryEntities = new ArrayList<>();

		List<HistoryEntity> forSaveHistoryEntity = new ArrayList<>();
		Map<LocalDate, List<HistoryEntity>> historySummaryMap = new TreeMap<>();
		List<HistorySummaryEntity> forSaveHistorySummaryEntity = new ArrayList<>();

		historyProcessingToHistorySummary(
				pitch, forward, tilt, memberEntity, forSaveHistoryEntity, historySummaryMap);

		saveSummaryHistory(
				memberEntity,
				historySummaryMap,
				forSaveHistorySummaryEntity,
				rawData,
				forSaveRawHistoryEntities);

		historyRepository.saveAll(forSaveHistoryEntity);
		historySummaryRepository.saveAll(forSaveHistorySummaryEntity);
		rawHistoryRepository.saveAll(forSaveRawHistoryEntities);
	}

	private void historyProcessingToHistorySummary(
			List<TreeMap<LocalDateTime, PoseStatus>> pitch,
			List<TreeMap<LocalDateTime, PoseStatus>> forward,
			List<TreeMap<LocalDateTime, PoseStatus>> tilt,
			MemberEntity memberEntity,
			List<HistoryEntity> forSaveHistoryEntity,
			Map<LocalDate, List<HistoryEntity>> historySummaryMap) {
		pitch.sort(Comparator.comparing(TreeMap::firstKey));
		forward.sort(Comparator.comparing(TreeMap::firstKey));
		tilt.sort(Comparator.comparing(TreeMap::firstKey));

		Set<LocalDate> uniqueLocalDate = new HashSet<>();

		for (TreeMap<LocalDateTime, PoseStatus> subHistory : pitch) {

			LocalDateTime startAt = subHistory.firstKey();
			LocalDate startDate = startAt.toLocalDate();
			uniqueLocalDate.add(startDate);
		}

		List<LocalDateTime> startAtListForCheckDuplicateHistory =
				historyRepository.findStartAtByDateList(uniqueLocalDate).stream()
						.map(HistoryEntity::getStartAt) // HistoryEntity에서 startAt(LocalDateTime) 추출
						.toList(); // LocalDateTime 리스트로 변환

		// subHistories 정리하기
		for (int i = 0; i < pitch.size(); i++) {
			TreeMap<LocalDateTime, PoseStatus> subPitchHistory = pitch.get(i);
			TreeMap<LocalDateTime, PoseStatus> subForwardHistory = forward.get(i);
			TreeMap<LocalDateTime, PoseStatus> subTiltHistory = tilt.get(i);

			Map<LocalDateTime, PoseStatus> pitchStatusMap = new TreeMap<>();
			Map<LocalDateTime, PoseStatus> forwardStatusMap = new TreeMap<>();
			Map<LocalDateTime, PoseStatus> tiltStatusMap = new TreeMap<>();

			Map<PoseStatus, Integer> poseCountMap = new HashMap<>();
			Map<PoseStatus, Long> poseTimeMap = new HashMap<>();

			LocalDateTime startAt = subPitchHistory.firstKey();
			LocalDateTime endAt = subPitchHistory.lastKey();

			if (startAtListForCheckDuplicateHistory.contains(startAt)) {
				//                System.out.println("중복된 히스토리");
				continue;
			}

			//            if (subHistory.get(startAt) != PoseStatus.valueOf("START")
			//                    || subHistory.get(endAt) != PoseStatus.valueOf("END")) {
			//
			//                throw new NotSupportHistoryException();
			//            }

			LocalDate startDate = startAt.toLocalDate();

			changeStartAndEndStatus(pitchStatusMap, startAt, endAt, subPitchHistory);
			changeStartAndEndStatus(forwardStatusMap, startAt, endAt, subForwardHistory);
			changeStartAndEndStatus(tiltStatusMap, startAt, endAt, subTiltHistory);

			// history 만들기
			HistoryEntity newHistory =
					HistoryEntity.builder()
							.member(memberEntity)
							.measuredTime(ChronoUnit.SECONDS.between(startAt, endAt))
							.startAt(startAt)
							.endAt(endAt)
							.date(startDate)
							.pitchStatusMap(pitchStatusMap)
							.forwardStatusMap(forwardStatusMap)
							.tiltingStatusMap(tiltStatusMap)
							.poseCountMap(poseCountMap)
							.poseTimerMap(poseTimeMap)
							.build();

			calculateSubHistory(
					"DOWNNORMAL", startAt, subPitchHistory, pitchStatusMap, poseCountMap, poseTimeMap, endAt);
			calculateSubHistory(
					"FORWARDNORMAL",
					startAt,
					subForwardHistory,
					forwardStatusMap,
					poseCountMap,
					poseTimeMap,
					endAt);
			calculateSubHistory(
					"TILTNORMAL", startAt, subTiltHistory, tiltStatusMap, poseCountMap, poseTimeMap, endAt);

			//            newHistory.updateHistoryPoint();

			// history 저장
			forSaveHistoryEntity.add(newHistory);
			historySummaryMap.put(
					startDate, historySummaryMap.getOrDefault(startDate, new ArrayList<>()));
			historySummaryMap.get(startDate).add(newHistory);
		}
	}

	private static void changeStartAndEndStatus(
			Map<LocalDateTime, PoseStatus> map,
			LocalDateTime startAt,
			LocalDateTime endAt,
			TreeMap<LocalDateTime, PoseStatus> subHistory) {
		map.put(startAt, PoseStatus.valueOf("START"));
		map.put(endAt, PoseStatus.valueOf("END"));

		subHistory.remove(startAt);
		subHistory.remove(endAt);
	}

	private static void calculateSubHistory(
			String FirstNORAMLString,
			LocalDateTime startAt,
			TreeMap<LocalDateTime, PoseStatus> subHistory,
			Map<LocalDateTime, PoseStatus> pitchStatusMap,
			Map<PoseStatus, Integer> poseCountMap,
			Map<PoseStatus, Long> poseTimeMap,
			LocalDateTime endAt) {
		LocalDateTime beforeStateTime = startAt;
		PoseStatus beforeState = PoseStatus.valueOf(FirstNORAMLString);
		for (Map.Entry<LocalDateTime, PoseStatus> neckEvent : subHistory.entrySet()) {
			LocalDateTime neckEventDate = neckEvent.getKey();
			PoseStatus neckEventStatus = neckEvent.getValue();

			if (neckEventStatus == beforeState) {
				continue;
			}

			pitchStatusMap.put(neckEventDate, neckEventStatus);
			poseCountMap.put(neckEventStatus, poseCountMap.getOrDefault(neckEventStatus, 0) + 1);

			long duration = ChronoUnit.SECONDS.between(beforeStateTime, neckEventDate);
			poseTimeMap.put(beforeState, (poseTimeMap.getOrDefault(beforeState, 0L) + (int) duration));

			beforeState = neckEventStatus;
			beforeStateTime = neckEventDate;
		}

		// 마지막은 따로 더하기
		long duration = ChronoUnit.SECONDS.between(beforeStateTime, endAt);
		poseTimeMap.put(beforeState, poseTimeMap.getOrDefault(beforeState, 0L) + (int) duration);
	}

	private MemberEntity checkExistMember(String memberId) {
		return memberRepository
				.findById(memberId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
	}

	private void saveSummaryHistory(
			MemberEntity memberEntity,
			Map<LocalDate, List<HistoryEntity>> historySummaryMap,
			List<HistorySummaryEntity> forSaveHistorySummaryEntity,
			List<Map<String, String>> rawData,
			List<RawHistoryEntity> forSaveRawHistoryEntities) {

		for (Map.Entry<LocalDate, List<HistoryEntity>> entry : historySummaryMap.entrySet()) {
			LocalDate date = entry.getKey();
			List<HistoryEntity> histories = entry.getValue();
			TreeMap<LocalDateTime, PoseStatus> poseStatusMap =
					(TreeMap<LocalDateTime, PoseStatus>) histories.get(0).getForwardStatusMap();
			LocalDateTime startTime = poseStatusMap.firstKey();
			LocalDateTime endTime = poseStatusMap.lastKey();

			List<Map<String, String>> subRawData = new ArrayList<>();

			for (Map<String, String> raw : rawData) {
				LocalDateTime timestamp = LocalDateTime.parse(raw.get("timestamp"));

				if (startTime.isBefore(timestamp) && endTime.isAfter(timestamp)) {
					subRawData.add(raw);
				}
			}

			double measuredTime = histories.stream().mapToDouble(HistoryEntity::getMeasuredTime).sum();

			Map<PoseStatus, Integer> poseCountMap = new HashMap<>();
			Map<PoseStatus, Long> poseTimerMap = new HashMap<>();
			TreeMap<LocalDateTime, PoseStatus> totalPitchStatusMap = new TreeMap<>();
			TreeMap<LocalDateTime, PoseStatus> totalForwardStatusMap = new TreeMap<>();
			TreeMap<LocalDateTime, PoseStatus> totalTiltStatusMap = new TreeMap<>();

			// 여기서 히스토리 동일한게 있는지 검증?
			for (HistoryEntity historyEntity : histories) {
				historyEntity
						.getPoseCountMap()
						.forEach((poseStatus, count) -> poseCountMap.merge(poseStatus, count, Integer::sum));
				historyEntity
						.getPoseTimerMap()
						.forEach((poseStatus, time) -> poseTimerMap.merge(poseStatus, time, Long::sum));
			}

			for (HistoryEntity historyEntity : histories) {
				totalPitchStatusMap.putAll(historyEntity.getPitchStatusMap());
				totalForwardStatusMap.putAll(historyEntity.getForwardStatusMap());
				totalTiltStatusMap.putAll(historyEntity.getTiltingStatusMap());
			}

			Optional<GoalEntity> activeGoal =
					getActiveGoalService.execute(memberEntity.getId(), totalPitchStatusMap.firstKey());
			Optional<HistorySummaryEntity> findSummary =
					historySummaryRepository.findByMemberAndDate(memberEntity.getId(), date);

			HistorySummaryEntity historySummaryEntity;
			if (findSummary.isEmpty()) {
				if (activeGoal.isEmpty()) {
					historySummaryEntity =
							HistorySummaryEntity.builder().member(memberEntity).date(date).build();

				} else {
					historySummaryEntity =
							HistorySummaryEntity.builder()
									.member(memberEntity)
									.goal(activeGoal.get())
									.date(date)
									.build();
				}

			} else {
				historySummaryEntity = findSummary.get();
			}

			RawHistoryEntity rawHistoryEntity =
					saveRawDataService.execute(memberEntity, historySummaryEntity, subRawData);

			forSaveRawHistoryEntities.add(rawHistoryEntity);

			historySummaryEntity.updateMeasuredTime(measuredTime);
			historySummaryEntity.updateSummary(
					totalPitchStatusMap,
					totalForwardStatusMap,
					totalTiltStatusMap,
					poseCountMap,
					poseTimerMap);
			historySummaryEntity.updateHistoryPointV3();
			historySummaryEntity.calculateAchievements();
			updateStreakService.execute(memberEntity, historySummaryEntity);

			forSaveHistorySummaryEntity.add(historySummaryEntity);
		}
	}
}
