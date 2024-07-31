package com.necklife.api.web.usecase.dto.response.history;

import java.util.List;

import com.necklife.api.entity.history.PoseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetMonthlyDetailResponse {

	private String month;
	private List<Day> days;

	@Data
	@AllArgsConstructor
	@Builder
	public static class Day {
		private String date;
		private int MeasurementTime;
		private int ForwardTime;
		private int BackwardTime;
		private int TiltedTime;
		private int ForwardCount;
		private int BackwardCount;
		private int TiltedCount;
		private int normalTime;
		private List<StatusChange> statusChanges;
	}

	@Data
	@AllArgsConstructor
	public static class StatusChange {
		private String time;
		private PoseStatus status;
	}
}
