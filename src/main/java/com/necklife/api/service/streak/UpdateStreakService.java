package com.necklife.api.service.streak;

import com.necklife.api.entity.goal.GoalType;
import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.streak.StreakEntity;
import com.necklife.api.repository.streak.StreakRepository;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateStreakService {

	private final StreakRepository streakRepository;

	public void execute(MemberEntity member, HistorySummaryEntity historySummaryEntity) {
		Optional<StreakEntity> optionalStreak = streakRepository.findByMember(member);
		StreakEntity streak;
		LocalDate activityDate = historySummaryEntity.getDate();

		//		boolean goalAchieved = Double.compare(historySummaryEntity.getGoalAchivedRate(), 1.0) == 0;

		Map<GoalType, Double> goalAchievements = historySummaryEntity.getGoalAchievements();

		boolean historyRecorded = historySummaryEntity.getMeasuredTime() > 0;

		if (optionalStreak.isPresent()) {
			streak = optionalStreak.get();

			//			// 목표 달성 스트릭 업데이트
			//			if (goalAchieved) {
			//				if (streak.getLastGoalStreakDate() != null
			//						&& streak.getLastGoalStreakDate().plusDays(1).equals(activityDate)) {
			//					streak.setCurrentGoalStreak(streak.getCurrentGoalStreak() + 1);
			//				} else {
			//					streak.setCurrentGoalStreak(1); // 새로운 스트릭 시작
			//				}
			//				streak.setLastGoalStreakDate(activityDate);
			//
			//				if (streak.getCurrentGoalStreak() > streak.getMaxGoalStreak()) {
			//					streak.setMaxGoalStreak(streak.getCurrentGoalStreak());
			//				}
			//			} else {
			//				streak.setCurrentGoalStreak(0); // 스트릭이 끊김
			//			}

			// 목표 달성 스트릭 업데이트
			for (GoalType goalType : goalAchievements.keySet()) {
				if (goalAchievements.get(goalType) == 1.0) {
					if (streak.getLastGoalStreakDate().get(goalType) != null
							&& streak.getLastGoalStreakDate().get(goalType).plusDays(1).equals(activityDate)) {

						Map<GoalType, Integer> currentGoalStreak = streak.getCurrentGoalStreak();
						currentGoalStreak.put(goalType, currentGoalStreak.get(goalType) + 1);

					} else {
						Map<GoalType, Integer> currentGoalStreak = streak.getCurrentGoalStreak(); // 새로운 스트릭 시작

						currentGoalStreak.put(goalType, 1);
					}
					Map<GoalType, LocalDate> lastGoalStreakDate = streak.getLastGoalStreakDate();
					lastGoalStreakDate.put(goalType, activityDate);

					if (streak.getCurrentGoalStreak().get(goalType)
							> streak.getMaxGoalStreak().get(goalType)) {
						streak.setMaxGoalStreak(streak.getCurrentGoalStreak());
					}
				} else {
					Map<GoalType, Integer> currentGoalStreak = streak.getCurrentGoalStreak(); // 스트릭이 끊김
					currentGoalStreak.put(goalType, 0);
				}
			}

			// 히스토리 기록 스트릭 업데이트
			if (historyRecorded) {
				if (streak.getLastHistoryStreakDate() != null
						&& streak.getLastHistoryStreakDate().plusDays(1).equals(activityDate)) {
					streak.setCurrentHistoryStreak(streak.getCurrentHistoryStreak() + 1);
					streak.setLastHistoryStreakDate(activityDate);
				} else if (streak.getLastHistoryStreakDate() != null
						&& (streak.getLastHistoryStreakDate().equals(activityDate)
								|| streak.getLastHistoryStreakDate().isAfter(activityDate))) {
					// 그대로 do nothing
				} else {
					streak.setCurrentHistoryStreak(1); // 새로운 스트릭 시작
					streak.setLastHistoryStreakDate(activityDate);
				}

				if (streak.getCurrentHistoryStreak() > streak.getMaxHistoryStreak()) {
					streak.setMaxHistoryStreak(streak.getCurrentHistoryStreak());
				}
			} else {
				streak.setCurrentHistoryStreak(0); // 스트릭이 끊김
			}

		} else {

			Map<GoalType, LocalDate> lastGoalStreakDate = new HashMap<>();
			Map<GoalType, Integer> currentGoalStreak = new HashMap<>();
			Map<GoalType, Integer> maxGoalStreak = new HashMap<>();

			for (GoalType goalType : GoalType.values()) {

				if (goalAchievements.get(goalType) == 1.0) {
					lastGoalStreakDate.put(goalType, activityDate);
					currentGoalStreak.put(goalType, 1);
					maxGoalStreak.put(goalType, 1);
				} else {
					lastGoalStreakDate.put(goalType, null);
					currentGoalStreak.put(goalType, 0);
					maxGoalStreak.put(goalType, 0);
				}
			}

			streak =
					StreakEntity.builder()
							.member(member)
							.lastGoalStreakDate(lastGoalStreakDate)
							.currentGoalStreak(currentGoalStreak)
							.maxGoalStreak(maxGoalStreak)
							.lastHistoryStreakDate(historyRecorded ? activityDate : null)
							.currentHistoryStreak(historyRecorded ? 1 : 0)
							.maxHistoryStreak(historyRecorded ? 1 : 0)
							.build();
		}

		streakRepository.save(streak);
	}
}
