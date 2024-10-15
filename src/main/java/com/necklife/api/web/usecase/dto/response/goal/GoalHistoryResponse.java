package com.necklife.api.web.usecase.dto.response.goal;

import com.necklife.api.entity.goal.GoalType;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class GoalHistoryResponse {

	private TreeMap<LocalDate, Map<GoalType, Double>> goalHistories;
}
