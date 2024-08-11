package com.necklife.api.web.dto.request.goal;

import com.necklife.api.entity.goal.GoalType;
import com.necklife.api.web.usecase.dto.response.goal.GoalResponse;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoalRequest {

    private List<GoalDetailRequestDTO> goals;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static public class GoalDetailRequestDTO {

        private Integer order;           // 목표 ID
        private String type;          // 목표 유형
        private String description;   // 목표 설명
        private Double target_value;   // 목표 타겟 값
    }
}
