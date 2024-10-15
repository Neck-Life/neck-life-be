package com.necklife.api.service.history;

import com.necklife.api.entity.goal.GoalEntity;
import com.necklife.api.entity.history.HistoryEntity;
import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.entity.history.PoseStatus;
import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.history.HistoryRepository;
import com.necklife.api.repository.history.HistorySummaryRepository;
import com.necklife.api.service.goal.GetActiveGoalService;
import com.necklife.api.service.streak.UpdateStreakService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class SaveHistoryService {

	private final HistoryRepository historyRepository;
	private final HistorySummaryRepository historySummaryRepository;
	private final GetActiveGoalService getActiveGoalService;
	private final UpdateStreakService updateStreakService;

	@Transactional
	public void execute(MemberEntity memberEntity, List<TreeMap<LocalDateTime, PoseStatus>> history) {

		List<HistoryEntity> forSaveHistoryEntity = new ArrayList<>();
		Map<LocalDate, List<HistoryEntity>> historySummaryMap = new TreeMap<>();
		List<HistorySummaryEntity> forSaveHistorySummaryEntity = new ArrayList<>();

		history.sort(Comparator.comparing(TreeMap::firstKey));

		Set<LocalDate> uniqueLocalDate = new HashSet<>();

		for (TreeMap<LocalDateTime, PoseStatus> subHistory : history) {

			LocalDateTime startAt = subHistory.firstKey();
			LocalDate startDate = startAt.toLocalDate();
			uniqueLocalDate.add(startDate);
		}

		List<LocalDateTime> startAtListForCheckDuplicateHistory =
				historyRepository.findStartAtByDateList(uniqueLocalDate).stream()
						.map(HistoryEntity::getStartAt) // HistoryEntity에서 startAt(LocalDateTime) 추출
						.toList(); // LocalDateTime 리스트로 변환

		// subHistories 정리하기
		for (TreeMap<LocalDateTime, PoseStatus> subHistory : history) {
			Map<LocalDateTime, PoseStatus> poseStatusMap = new TreeMap<>();
			Map<PoseStatus, Integer> poseCountMap = new HashMap<>();
			Map<PoseStatus, Long> poseTimeMap = new HashMap<>();

			LocalDateTime startAt = subHistory.firstKey();
			LocalDateTime endAt = subHistory.lastKey();

			if (startAtListForCheckDuplicateHistory.contains(startAt)) {
				System.out.println("중복된 히스토리");
				continue;
			}

			//			if (subHistory.get(startAt) != PoseStatus.valueOf("START")
			//					|| subHistory.get(endAt) != PoseStatus.valueOf("END")) {
			//
			//				throw new NotSupportHistoryException();
			//			}

			LocalDate startDate = startAt.toLocalDate();

			poseStatusMap.put(startAt, PoseStatus.valueOf("START"));
			poseStatusMap.put(endAt, PoseStatus.valueOf("END"));

			subHistory.remove(startAt);
			subHistory.remove(endAt);

			// history 만들기
			HistoryEntity newHistory =
					HistoryEntity.builder()
							.member(memberEntity)
							.measuredTime(ChronoUnit.SECONDS.between(startAt, endAt))
							.startAt(startAt)
							.endAt(endAt)
							.date(startDate)
							.poseStatusMap(poseStatusMap)
							.poseCountMap(poseCountMap)
							.poseTimerMap(poseTimeMap)
							.build();

			LocalDateTime beforeStateTime = startAt;
			PoseStatus beforeState = PoseStatus.valueOf("NORMAL");
			for (Map.Entry<LocalDateTime, PoseStatus> neckEvent : subHistory.entrySet()) {
				LocalDateTime neckEventDate = neckEvent.getKey();
				PoseStatus neckEventStatus = neckEvent.getValue();

				if (neckEventStatus == beforeState) {
					continue;
				}

				poseStatusMap.put(neckEventDate, neckEventStatus);
				poseCountMap.put(neckEventStatus, poseCountMap.getOrDefault(neckEventStatus, 0) + 1);

				long duration = ChronoUnit.SECONDS.between(beforeStateTime, neckEventDate);
				poseTimeMap.put(beforeState, (poseTimeMap.getOrDefault(beforeState, 0L) + (int) duration));

				beforeState = neckEventStatus;
				beforeStateTime = neckEventDate;
			}

			// 마지막은 따로 더하기
			long duration = ChronoUnit.SECONDS.between(beforeStateTime, endAt);
			poseTimeMap.put(beforeState, poseTimeMap.getOrDefault(beforeState, 0L) + (int) duration);

			newHistory.updateHistoryPoint();

			// history 저장
			forSaveHistoryEntity.add(newHistory);
			historySummaryMap.put(
					startDate, historySummaryMap.getOrDefault(startDate, new ArrayList<>()));
			historySummaryMap.get(startDate).add(newHistory);
		}

		saveSummaryHistory(memberEntity, historySummaryMap, forSaveHistorySummaryEntity);

		// todo 여기서 중복되는 history인지 검증이 필요함 이걸 DB한테 맡길 수 있지 않을까 근데 여기에 또 이걸 historySummary에 넣으니까 그 전에
		// 탐지해야되나
		historyRepository.saveAll(forSaveHistoryEntity);
		historySummaryRepository.saveAll(forSaveHistorySummaryEntity);
	}

	private void saveSummaryHistory(
			MemberEntity memberEntity,
			Map<LocalDate, List<HistoryEntity>> historySummaryMap,
			List<HistorySummaryEntity> forSaveHistorySummaryEntity) {

		for (Map.Entry<LocalDate, List<HistoryEntity>> entry : historySummaryMap.entrySet()) {
			LocalDate date = entry.getKey();
			List<HistoryEntity> histories = entry.getValue();

			double measuredTime = histories.stream().mapToDouble(HistoryEntity::getMeasuredTime).sum();

			Map<PoseStatus, Integer> poseCountMap = new HashMap<>();
			Map<PoseStatus, Long> poseTimerMap = new HashMap<>();
			TreeMap<LocalDateTime, PoseStatus> totalPoseStatusMap = new TreeMap<>();

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
				totalPoseStatusMap.putAll(historyEntity.getPoseStatusMap());
			}

			Optional<GoalEntity> activeGoal =
					getActiveGoalService.execute(memberEntity.getId(), totalPoseStatusMap.firstKey());
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

			historySummaryEntity.updateMeasuredTime(measuredTime);
			historySummaryEntity.updateSummary(totalPoseStatusMap, poseCountMap, poseTimerMap);
			historySummaryEntity.updateHistoryPoint();
			historySummaryEntity.calculateAchievements();
			updateStreakService.execute(memberEntity, historySummaryEntity);

			forSaveHistorySummaryEntity.add(historySummaryEntity);
		}
	}
}
