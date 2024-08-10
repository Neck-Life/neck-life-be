package com.necklife.api.web.usecase.history;

import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.entity.history.PoseStatus;
import com.necklife.api.repository.history.HistorySummaryRepository;
import com.necklife.api.web.usecase.dto.request.history.GetMonthlyHistoryRequest;
import com.necklife.api.web.usecase.dto.response.history.GetMonthlyDetailResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMonthlyDetailUseCase {

	private final HistorySummaryRepository historySummaryRepository;

	public GetMonthlyDetailResponse execute(GetMonthlyHistoryRequest getMonthlyHistoryRequest) {
		String memberId = getMonthlyHistoryRequest.memberId();

		Integer year = getMonthlyHistoryRequest.year();
		Integer month = getMonthlyHistoryRequest.month();

		List<HistorySummaryEntity> historySummaryEntities =
				historySummaryRepository.findByMemberIdAndDateBetweenOrderByDate(
						memberId,
						LocalDate.of(year, month, 1),
						LocalDate.of(year, month, 1).plusMonths(1).minusDays(1));

		// 요약된 데이터를 바탕으로 일별 데이터 변환
		List<GetMonthlyDetailResponse.Day> daily =
				historySummaryEntities.stream()
						.map(
								summary -> {
									// 일별 데이터 변환
									LocalDate date = summary.getDate();
									double measurementTime = summary.getMeasuredTime();
									Map<PoseStatus, Integer> poseCountMap = summary.getTotalPoseCountMap();
									Map<PoseStatus, Long> poseTimerMap = summary.getTotalPoseTimerMap();
									TreeMap<LocalDateTime, PoseStatus> history =
											new TreeMap<>(summary.getTotalPoseStatusMap());
									int point = summary.getTotalHistoryPoint();

									return GetMonthlyDetailResponse.Day.builder()
											.date(date.getDayOfMonth())
											.measurementTime(measurementTime)
											.point(point)
											.poseCountMap(poseCountMap)
											.poseTimerMap(poseTimerMap)
											.history(history)
											.build();
								})
						.collect(Collectors.toList());

		return GetMonthlyDetailResponse.builder()
				.year(String.valueOf(year))
				.month(String.valueOf(month))
				.daily(daily)
				.build();
	}
}
