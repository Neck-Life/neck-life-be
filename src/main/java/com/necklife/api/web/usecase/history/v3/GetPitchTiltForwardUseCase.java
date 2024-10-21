package com.necklife.api.web.usecase.history.v3;

import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.entity.history.PoseStatus;
import com.necklife.api.repository.history.HistorySummaryRepository;
import com.necklife.api.web.usecase.dto.request.history.GetMonthlyHistoryRequest;
import com.necklife.api.web.usecase.dto.response.history.v3.GetMonthlyDetailPitchForwardTiltResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPitchTiltForwardUseCase {

	private final HistorySummaryRepository historySummaryRepository;

	public GetMonthlyDetailPitchForwardTiltResponse execute(
			GetMonthlyHistoryRequest getMonthlyHistoryRequest) {
		String memberId = getMonthlyHistoryRequest.memberId();

		Integer year = getMonthlyHistoryRequest.year();
		Integer month = getMonthlyHistoryRequest.month();

		List<HistorySummaryEntity> historySummaryEntities =
				historySummaryRepository.findByMemberIdAndDateBetweenOrderByDate(
						memberId,
						LocalDate.of(year, month, 1),
						LocalDate.of(year, month, 1).plusMonths(1).minusDays(1));

		// 요약된 데이터를 바탕으로 일별 데이터 변환
		List<GetMonthlyDetailPitchForwardTiltResponse.Day> daily =
				historySummaryEntities.stream()
						.map(
								summary -> {
									// 일별 데이터 변환
									LocalDate date = summary.getDate();
									double measurementTime = summary.getMeasuredTime();
									Map<PoseStatus, Integer> poseCountMap = summary.getTotalPoseCountMap();
									Map<PoseStatus, Long> poseTimerMap = summary.getTotalPoseTimerMap();

									Map<LocalDateTime, PoseStatus> totalPitchStatusMap =
											summary.getTotalPitchStatusMap();
									if (totalPitchStatusMap == null) {
										totalPitchStatusMap = new TreeMap<>();
									}

									Map<LocalDateTime, PoseStatus> totalForwardStatusMap =
											summary.getTotalForwardStatusMap();
									if (totalForwardStatusMap == null) {
										totalForwardStatusMap = new TreeMap<>();
									}

									Map<LocalDateTime, PoseStatus> totalTiltStatusMap =
											summary.getTotalTiltStatusMap();
									if (totalTiltStatusMap == null) {
										totalTiltStatusMap = new TreeMap<>();
									}

									TreeMap<LocalDateTime, PoseStatus> pitch = new TreeMap<>(totalPitchStatusMap);

									TreeMap<LocalDateTime, PoseStatus> forward = new TreeMap<>(totalForwardStatusMap);

									TreeMap<LocalDateTime, PoseStatus> tilt = new TreeMap<>(totalTiltStatusMap);

									int point = summary.getTotalHistoryPoint();

									return GetMonthlyDetailPitchForwardTiltResponse.Day.builder()
											.date(date.getDayOfMonth())
											.measurementTime(measurementTime)
											.point(point)
											.poseCountMap(poseCountMap)
											.poseTimerMap(poseTimerMap)
											.forward(forward)
											.tilt(tilt)
											.pitch(pitch)
											.build();
								})
						.collect(Collectors.toList());

		return GetMonthlyDetailPitchForwardTiltResponse.builder()
				.year(String.valueOf(year))
				.month(String.valueOf(month))
				.daily(daily)
				.build();
	}
}
