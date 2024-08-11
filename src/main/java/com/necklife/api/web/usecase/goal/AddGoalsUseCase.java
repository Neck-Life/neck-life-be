package com.necklife.api.web.usecase.goal;

import com.necklife.api.entity.goal.GoalEntity;
import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.service.goal.SetNewGoalService;
import com.necklife.api.web.usecase.dto.response.goal.GoalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddGoalsUseCase {

    private final SetNewGoalService addGoalService;

    public GoalResponse execute(String MemberId, List<GoalEntity.GoalDetail> newGoalDetails) {
        LocalDateTime now = LocalDateTime.now();
        HistorySummaryEntity changedHistory = addGoalService.execute(MemberId, now, newGoalDetails);

        List<GoalResponse.GoalDetailResponseDTO> changedGoals = changedHistory.getGoal().getGoals().stream().map(goal -> GoalResponse.GoalDetailResponseDTO.builder()
                .order(goal.getOrders())
                .type(goal.getType())
                .description(goal.getDescription())
                .targetValue(goal.getTargetValue())
                .build()).toList();


        return GoalResponse.builder().goals(changedGoals).build();
    }
}
