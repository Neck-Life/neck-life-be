package com.necklife.api.service.goal;

import com.necklife.api.entity.goal.GoalEntity;
import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.goal.GoalRepository;
import com.necklife.api.repository.history.HistorySummaryRepository;
import com.necklife.api.repository.member.MemberRepository;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateGoalService {

	private final GoalRepository goalRepository;
	private final GetActiveGoalService GetActiveGoalService;
	private final HistorySummaryRepository historySummaryRepository;
	private final MemberRepository memberRepository;

	public HistorySummaryEntity execute(
			String memberId, LocalDateTime date, List<GoalEntity.GoalDetail> newGoalDetails) {

		MemberEntity member =
				memberRepository
						.findById(memberId)
						.orElseThrow(() -> new IllegalArgumentException("Member not found"));

		Optional<GoalEntity> currentGoalOpt = GetActiveGoalService.execute(member.getId(), date);

		List<GoalEntity.GoalDetail> sortedGoalDetails = new ArrayList<>(newGoalDetails);
		sortedGoalDetails.sort(Comparator.comparing(GoalEntity.GoalDetail::getOrders));

		GoalEntity goalEntity;

		if (currentGoalOpt.isPresent()) {
			// 현재 목표가 존재하면 기존 목표 리스트에 새 목표를 추가
			goalEntity = currentGoalOpt.get();

			// 기존 목표 종료
			goalEntity.updateEffectiveTo(date);
			goalRepository.save(goalEntity);

			sortedGoalDetails.forEach(goal -> goal.setOrders(sortedGoalDetails.indexOf(goal) + 1));

			// 새로운 목표로 기존 목표를 복사
			GoalEntity newGoalEntity =
					GoalEntity.builder()
							.member(member)
							.effectiveFrom(date)
							.effectiveTo(null)
							.goals(sortedGoalDetails)
							.build();

			goalRepository.save(newGoalEntity);

			// 현재 진행중인 일정의 목표를 수정
			return updateNewGoalOnHistorySummary(member, newGoalEntity);

		} else {
			// 유효한 목표가 없으면 새로운 목표 리스트로 시작
			sortedGoalDetails.forEach(goal -> goal.setOrders(sortedGoalDetails.indexOf(goal) + 1));

			GoalEntity savedGoal =
					goalRepository.save(
							GoalEntity.builder()
									.member(member)
									.goals(sortedGoalDetails)
									.effectiveFrom(date)
									.effectiveTo(null)
									.build());

			// 현재 진행중인 일정의 목표를 수정
			return updateNewGoalOnHistorySummary(member, savedGoal);
		}
	}

	private HistorySummaryEntity updateNewGoalOnHistorySummary(
			MemberEntity member, GoalEntity newGoalEntity) {

		HistorySummaryEntity historySummaryEntity =
				historySummaryRepository
						.findTopByMemberIdOrderByDateDesc(member.getId())
						.orElseThrow(() -> new IllegalArgumentException("HistorySummary not found"));

		historySummaryEntity.changeGoals(newGoalEntity);
		return historySummaryRepository.save(historySummaryEntity);
	}
}
