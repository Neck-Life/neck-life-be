package com.necklife.api.web.usecase.dto.response.history;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetYearDetailResponse {
	private String year;
	private List<Month> months;

	@Data
	@AllArgsConstructor
	@Builder
	public static class Month {
		private String month;
		private int MeasurementTime;
		private int ForwardTime;
		private int BackwardTime;
		private int TiltedTime;
		private int ForwardCount;
		private int BackwardCount;
		private int TiltedCount;
		private int NormalTime;
	}
}
