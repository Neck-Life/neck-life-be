package com.necklife.api.web.usecase.dto.response.history;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class GetHistoryPointResponse {
	private int totalScore;
	private MonthData thisMonth;
	private MonthData lastMonth;

	@Data
	@AllArgsConstructor
	public static class MonthData {
		private int totalMeasurementTime;
		private int totalForwardTime;
		private int totalBackwardTime;
		private int totalTiltedTime;
		private int totalForwardCount;
		private int totalBackwardCount;
		private int totalTiltedCount;
	}
}
