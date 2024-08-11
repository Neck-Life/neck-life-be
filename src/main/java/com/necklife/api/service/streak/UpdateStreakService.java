package com.necklife.api.service.streak;

import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.streak.StreakEntity;
import com.necklife.api.repository.streak.StreakRepository;
import java.time.LocalDate;
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

		boolean goalAchieved = Double.compare(historySummaryEntity.getGoalAchivedRate(), 1.0) == 0;
		boolean historyRecorded = historySummaryEntity.getMeasuredTime() > 0;

		if (optionalStreak.isPresent()) {
			streak = optionalStreak.get();

			// 목표 달성 스트릭 업데이트
			if (goalAchieved) {
				if (streak.getLastGoalStreakDate() != null
						&& streak.getLastGoalStreakDate().plusDays(1).equals(activityDate)) {
					streak.setCurrentGoalStreak(streak.getCurrentGoalStreak() + 1);
				} else {
					streak.setCurrentGoalStreak(1); // 새로운 스트릭 시작
				}
				streak.setLastGoalStreakDate(activityDate);

				if (streak.getCurrentGoalStreak() > streak.getMaxGoalStreak()) {
					streak.setMaxGoalStreak(streak.getCurrentGoalStreak());
				}
			} else {
				streak.setCurrentGoalStreak(0); // 스트릭이 끊김
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
			streak =
					StreakEntity.builder()
							.member(member)
							.lastGoalStreakDate(goalAchieved ? activityDate : null)
							.currentGoalStreak(goalAchieved ? 1 : 0)
							.maxGoalStreak(goalAchieved ? 1 : 0)
							.lastHistoryStreakDate(historyRecorded ? activityDate : null)
							.currentHistoryStreak(historyRecorded ? 1 : 0)
							.maxHistoryStreak(historyRecorded ? 1 : 0)
							.build();
		}

		streakRepository.save(streak);
	}
}
