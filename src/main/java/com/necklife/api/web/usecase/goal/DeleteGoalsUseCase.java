package com.necklife.api.web.usecase.goal;

import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.service.goal.DeleteGoalService;
import com.necklife.api.web.usecase.dto.response.goal.GoalResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteGoalsUseCase {

	private final DeleteGoalService deleteGoalService;

	public GoalResponse execute(String memberId, List<Integer> goalIds) {
		LocalDateTime now = LocalDateTime.now();
		// 목표 삭제
		HistorySummaryEntity changedHistory = deleteGoalService.execute(memberId, now, goalIds);

		List<GoalResponse.GoalDetailResponseDTO> changedGoals =
				changedHistory.getGoal().getGoals().stream()
						.map(
								goal ->
										GoalResponse.GoalDetailResponseDTO.builder()
												.order(goal.getOrders())
												.type(goal.getType())
												.description(goal.getDescription())
												.targetValue(goal.getTargetValue())
												.build())
						.toList();

		return GoalResponse.builder().goals(changedGoals).build();
	}
}
