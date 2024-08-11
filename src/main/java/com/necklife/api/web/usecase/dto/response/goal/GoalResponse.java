package com.necklife.api.web.usecase.dto.response.goal;

import com.necklife.api.entity.goal.GoalType;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class GoalResponse {

	private List<GoalDetailResponseDTO> goals;

	@Data
	@Builder
	@Getter
	public static class GoalDetailResponseDTO {

		private Integer order; // 목표 ID
		private GoalType type; // 목표 유형
		private String description; // 목표 설명
		private Double targetValue; // 목표 타겟 값
	}
}
