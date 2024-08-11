package com.necklife.api.web.usecase.dto.response.goal;


import com.necklife.api.entity.goal.GoalType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Builder
@Getter
public class GoalResponse {

    private List<GoalDetailResponseDTO> goals;


    @Data
    @Builder
    @Getter
    static public class GoalDetailResponseDTO {

        private Integer order;           // 목표 ID
        private GoalType type;          // 목표 유형
        private String description;   // 목표 설명
        private Double targetValue;   // 목표 타겟 값
    }
}
