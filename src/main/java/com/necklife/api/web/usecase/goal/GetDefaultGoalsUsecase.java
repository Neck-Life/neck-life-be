package com.necklife.api.web.usecase.goal;

import com.necklife.api.entity.goal.GoalType;
import com.necklife.api.web.usecase.dto.response.goal.GoalResponse;
import com.necklife.api.web.usecase.dto.response.goal.GoalResponse.GoalDetailResponseDTO;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetDefaultGoalsUsecase {

	public GoalResponse execute() {
		// 기본 목표 생성
		GoalDetailResponseDTO goal1 =
				GoalDetailResponseDTO.builder()
						.order(1)
						.type(GoalType.MEASUREMENT)
						.description("하루에 2시간 이상 측정하기")
						.targetValue(2.0)
						.build();

		GoalDetailResponseDTO goal2 =
				GoalDetailResponseDTO.builder()
						.order(2)
						.type(GoalType.SCORE)
						.description("오늘의 자세 점수 80점 이상")
						.targetValue(80.0)
						.build();

		// 기본 목표 리스트 생성
		List<GoalDetailResponseDTO> defaultGoals = Arrays.asList(goal1, goal2);

		// GoalResponse로 감싸서 반환
		return GoalResponse.builder().goals(defaultGoals).build();
	}
}
