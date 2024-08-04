package com.necklife.api.web.usecase.dto.response.history;

import com.necklife.api.entity.history.PoseStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetMonthlyDetailResponse {

	private String year;
	private String month;
	private List<Day> daily;

	@Data
	@AllArgsConstructor
	@Builder
	public static class Day {
		private double measurementTime;
		private Map<PoseStatus, Integer> poseCountMap;
		private Map<PoseStatus, Long> poseTimerMap;
		private Map<LocalDateTime, PoseStatus> history;
	}
}
