package com.necklife.api.web.usecase.dto.response.goal;

import java.time.LocalDate;
import java.util.TreeMap;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class GoalHistoryResponse {

	private TreeMap<LocalDate, Double> goalHistories;
}
