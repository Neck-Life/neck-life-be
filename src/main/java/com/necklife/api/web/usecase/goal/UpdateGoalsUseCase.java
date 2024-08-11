package com.necklife.api.web.usecase.goal;

import com.necklife.api.entity.goal.GoalEntity;
import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.service.goal.UpdateGoalService;
import com.necklife.api.web.usecase.dto.response.goal.GoalResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateGoalsUseCase {

	private final UpdateGoalService updateGoalService;

	public GoalResponse execute(String MemberId, List<GoalEntity.GoalDetail> newGoalDetails) {
		LocalDateTime now = LocalDateTime.now();
		HistorySummaryEntity changedHistory = updateGoalService.execute(MemberId, now, newGoalDetails);

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
