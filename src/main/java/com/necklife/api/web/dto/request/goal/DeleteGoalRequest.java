package com.necklife.api.web.dto.request.goal;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteGoalRequest {

    private List<Integer> goalIds;
}
