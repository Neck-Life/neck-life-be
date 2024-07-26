package com.necklife.api.web.usecase.dto.response.history;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class GetYearDetailResponse {
	private String year;
	private List<Month> months;

	@Data
	@AllArgsConstructor
	public static class Month {
		private String month;
		private int totalMeasurementTime;
		private int totalForwardTime;
		private int totalBackwardTime;
		private int totalTiltedTime;
		private int totalForwardCount;
		private int totalBackwardCount;
		private int totalTiltedCount;
	}
}
