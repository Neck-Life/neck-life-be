package com.necklife.api.web.dto.request.goal;

import java.util.List;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteGoalRequest {

	private List<Integer> goalIds;
}
