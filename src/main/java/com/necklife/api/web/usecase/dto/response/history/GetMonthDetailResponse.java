package com.necklife.api.web.usecase.dto.response.history;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class GetMonthDetailResponse {

	private String month;
	private List<Day> days;

	@Data
	@AllArgsConstructor
	public static class Day {
		private String date;
		private int totalMeasurementTime;
		private int totalForwardTime;
		private int totalBackwardTime;
		private int totalTiltedTime;
		private int totalForwardCount;
		private int totalBackwardCount;
		private int totalTiltedCount;
		private List<StatusChange> statusChanges;
	}

	@Data
	@AllArgsConstructor
	public static class StatusChange {
		private String time;
		private int duration;
		private PostureStatus status;
	}
}
