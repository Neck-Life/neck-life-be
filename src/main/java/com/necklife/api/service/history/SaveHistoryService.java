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
import com.necklife.api.web.exception.NotSupportHistoryException;
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


	private final int POINT_WEIGHT = 3;
	private final int DEFAULT_POINT = 30;

	@Transactional
	public void execute(MemberEntity memberEntity, List<TreeMap<LocalDateTime, PoseStatus>> history) {

		List<HistoryEntity> forSaveHistoryEntity = new ArrayList<>();
		Map<LocalDate, List<HistoryEntity>> historySummaryMap = new TreeMap<>();
		List<HistorySummaryEntity> forSaveHistorySummaryEntity = new ArrayList<>();

		history.sort(Comparator.comparing(o -> o.firstKey()));

		// subHistories 정리하기
		for (TreeMap<LocalDateTime, PoseStatus> subHistory : history) {
			Map<LocalDateTime, PoseStatus> poseStatusMap = new TreeMap<>();
			Map<PoseStatus, Integer> poseCountMap = new HashMap<>();
			Map<PoseStatus, Long> poseTimeMap = new HashMap<>();

			LocalDateTime startAt = subHistory.firstKey();
			LocalDateTime endAt = subHistory.lastKey();

			if (subHistory.get(startAt) != PoseStatus.valueOf("START")
					|| subHistory.get(endAt) != PoseStatus.valueOf("END")) {

				throw new NotSupportHistoryException();
			}

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

			int point =
					DEFAULT_POINT
							+ (int) (poseTimeMap.getOrDefault(PoseStatus.NORMAL, 0L) / 60)
							+ poseCountMap.getOrDefault(PoseStatus.NORMAL, 0) * POINT_WEIGHT;

			for (int count : poseCountMap.values()) {
				point -= count * POINT_WEIGHT;
			}
			newHistory.updateHistoryPoint(point);

			// history 저장
			forSaveHistoryEntity.add(newHistory);
			historySummaryMap.put(
					startDate, historySummaryMap.getOrDefault(startDate, new ArrayList<>()));
			historySummaryMap.get(startDate).add(newHistory);



		}

		saveSummaryHistory(memberEntity, historySummaryMap, forSaveHistorySummaryEntity);



		historyRepository.saveAll(forSaveHistoryEntity);
		historySummaryRepository.saveAll(forSaveHistorySummaryEntity);
	}

	private void saveSummaryHistory(
			MemberEntity memberEntity,
			Map<LocalDate, List<HistoryEntity>> historySummaryMap,
			List<HistorySummaryEntity> forSaveHistorySummaryEntity
			) {



		for (Map.Entry<LocalDate, List<HistoryEntity>> entry : historySummaryMap.entrySet()) {
			LocalDate date = entry.getKey();
			List<HistoryEntity> histories = entry.getValue();

			double measuredTime = histories.stream().mapToDouble(HistoryEntity::getMeasuredTime).sum();

			Map<PoseStatus, Integer> poseCountMap = new HashMap<>();
			Map<PoseStatus, Long> poseTimerMap = new HashMap<>();
			TreeMap<LocalDateTime, PoseStatus> totalPoseStatusMap = new TreeMap<>();

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

			int totalHistoryPoint = histories.stream().mapToInt(HistoryEntity::getHistoryPoint).sum();


			Optional<GoalEntity> activeGoal = getActiveGoalService.execute(memberEntity.getId(), totalPoseStatusMap.firstKey());
			Optional<HistorySummaryEntity> findSummary = historySummaryRepository
					.findByMemberAndDate(memberEntity.getId(), date);

			HistorySummaryEntity historySummaryEntity;
			if (findSummary.isEmpty()) {
				if (activeGoal.isEmpty()) {
					historySummaryEntity = HistorySummaryEntity.builder().member(memberEntity)
							.date(date).build();

				} else {
					historySummaryEntity = HistorySummaryEntity.builder().member(memberEntity)
							.goal(activeGoal.get()).date(date).build();
				}

			}else
			{
				historySummaryEntity = findSummary.get();
			}



			historySummaryEntity.updateMeasuredTime(measuredTime);
			historySummaryEntity.updateSummary(totalPoseStatusMap, poseCountMap, poseTimerMap);
			historySummaryEntity.updateHistoryPoint(totalHistoryPoint);
			historySummaryEntity.calculateAchievements();
			updateStreakService.execute(memberEntity, historySummaryEntity);


			forSaveHistorySummaryEntity.add(historySummaryEntity);
		}
	}
}
