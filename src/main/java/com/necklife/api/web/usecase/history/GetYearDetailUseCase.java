package com.necklife.api.web.usecase.history;

import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.entity.history.PoseStatus;
import com.necklife.api.repository.history.HistorySummaryRepository;
import com.necklife.api.web.usecase.dto.request.history.GetYearHistoryRequest;
import com.necklife.api.web.usecase.dto.response.history.GetYearDetailResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetYearDetailUseCase {

	private final HistorySummaryRepository historySummaryRepository;

	public GetYearDetailResponse execute(GetYearHistoryRequest getYearHistoryRequest) {
		String memberId = getYearHistoryRequest.memberId();
		Integer year = getYearHistoryRequest.year();

		// 날짜 형식 설정
		DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");

		// 해당 멤버, 연도에 대한 요약 데이터 조회
		LocalDate startDate = LocalDate.of(year, 1, 1);
		LocalDate endDate = startDate.plusYears(1).minusDays(1);
		List<HistorySummaryEntity> historySummaryEntities =
				historySummaryRepository.findByMemberIdAndDateBetweenOrderByDate(
						memberId, startDate, endDate);

		// 월별 데이터 그룹화
		Map<String, List<HistorySummaryEntity>> groupedByMonth =
				historySummaryEntities.stream()
						.collect(Collectors.groupingBy(summary -> summary.getDate().format(monthFormatter)));

		// 월별 데이터 변환
		List<GetYearDetailResponse.Month> months =
				groupedByMonth.entrySet().stream()
						.map(
								entry -> {
									String month = entry.getKey();
									List<HistorySummaryEntity> monthlySummaries = entry.getValue();

									// 측정 시간 계산
									double measurementTime =
											monthlySummaries.stream()
													.mapToDouble(HistorySummaryEntity::getMeasuredTime)
													.sum();

									// poseCountMap, poseTimerMap 설정
									Map<PoseStatus, Integer> poseCountMap = new HashMap<>();
									Map<PoseStatus, Long> poseTimerMap = new HashMap<>();

									for (HistorySummaryEntity summary : monthlySummaries) {
										summary
												.getTotalPoseCountMap()
												.forEach(
														(poseStatus, count) ->
																poseCountMap.merge(poseStatus, count, Integer::sum));
										summary
												.getTotalPoseTimerMap()
												.forEach(
														(poseStatus, time) -> poseTimerMap.merge(poseStatus, time, Long::sum));
									}

									return GetYearDetailResponse.Month.builder()
											.month(month)
											.measurementTime(measurementTime)
											.poseCountMap(poseCountMap)
											.poseTimerMap(poseTimerMap)
											.build();
								})
						.collect(Collectors.toList());

		return GetYearDetailResponse.builder().year(String.valueOf(year)).months(months).build();
	}
}
