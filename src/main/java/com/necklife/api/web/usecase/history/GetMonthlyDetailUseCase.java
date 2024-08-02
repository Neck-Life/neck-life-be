package com.necklife.api.web.usecase.history;

import com.necklife.api.entity.history.HistoryEntity;
import com.necklife.api.entity.history.PoseStatus;
import com.necklife.api.repository.history.HistoryRepository;
import com.necklife.api.web.usecase.dto.request.history.GetMonthlyHistoryRequest;
import com.necklife.api.web.usecase.dto.response.history.GetMonthlyDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetMonthlyDetailUseCase {

	private final HistoryRepository historyRepository;

	public GetMonthlyDetailResponse execute(GetMonthlyHistoryRequest getMonthlyHistoryRequest) {
		String memberId = getMonthlyHistoryRequest.memberId();
		Integer year = getMonthlyHistoryRequest.year();
		Integer month = getMonthlyHistoryRequest.month();

		// 날짜 형식 설정
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// 해당 멤버, 연도, 월에 대한 히스토리 데이터 조회
		List<HistoryEntity> histories = historyRepository.findByMemberIdAndYearAndMonth(memberId, year, month);

		// 일별 데이터 그룹화
		Map<LocalDate, List<HistoryEntity>> groupedByDate = histories.stream()
				.collect(Collectors.groupingBy(history -> history.getStartAt().toLocalDate()));

		// 일별 데이터 변환
		List<GetMonthlyDetailResponse.Day> daily = groupedByDate.entrySet().stream()
				.map(entry -> {
					List<HistoryEntity> dailyHistories = entry.getValue();

					// 측정 시간 계산
					double measurementTime = dailyHistories.stream().mapToDouble(HistoryEntity::getMeasuredTime).sum();

					// poseCountMap, poseTimerMap, history 설정
					Map<PoseStatus, Integer> poseCountMap = new HashMap<>();
					Map<PoseStatus, Long> poseTimerMap = new HashMap<>();
					Map<LocalDateTime, PoseStatus> history = new TreeMap<>();

					for (HistoryEntity historyEntity : dailyHistories) {
						historyEntity.getPoseCountMap().forEach((poseStatus, count) ->
								poseCountMap.merge(poseStatus, count, Integer::sum));
						historyEntity.getPoseTimerMap().forEach((poseStatus, time) ->
								poseTimerMap.merge(poseStatus, time, Long::sum));
                        history.putAll(historyEntity.getPoseStatusMap());
					}

					return GetMonthlyDetailResponse.Day.builder()
							.measurementTime(measurementTime)
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
