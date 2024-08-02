package com.necklife.api.web.usecase.dto.response.history;

import java.util.List;
import java.util.Map;

import com.necklife.api.entity.history.PoseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class GetYearDetailResponse {

	private String year;
	private List<Month> months;

	@Data
	@AllArgsConstructor
	@Builder
	public static class Month {
		private String month;
		private double measurementTime;
		private Map<PoseStatus, Integer> poseCountMap;
		private Map<PoseStatus, Long> poseTimerMap;
	}
}
