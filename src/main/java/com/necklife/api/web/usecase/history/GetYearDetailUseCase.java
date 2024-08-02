package com.necklife.api.web.usecase.history;

import com.necklife.api.entity.history.HistoryEntity;
import com.necklife.api.entity.history.PoseStatus;
import com.necklife.api.repository.history.HistoryRepository;
import com.necklife.api.web.usecase.dto.request.history.GetYearHistoryRequest;
import com.necklife.api.web.usecase.dto.response.history.GetYearDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetYearDetailUseCase {

	private final HistoryRepository historyRepository;

	public GetYearDetailResponse execute(GetYearHistoryRequest getYearHistoryRequest) {
		String memberId = getYearHistoryRequest.memberId();
		Integer year = getYearHistoryRequest.year();

		// 날짜 형식 설정
		DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");

		// 해당 멤버, 연도에 대한 히스토리 데이터 조회
		List<HistoryEntity> histories = historyRepository.findByMemberIdAndYear(memberId, year);

		// 월별 데이터 그룹화
		Map<String, List<HistoryEntity>> groupedByMonth = histories.stream()
				.collect(Collectors.groupingBy(history -> history.getStartAt().format(monthFormatter)));

		// 월별 데이터 변환
		List<GetYearDetailResponse.Month> months = groupedByMonth.entrySet().stream()
				.map(entry -> {
					String month = entry.getKey();
					List<HistoryEntity> monthlyHistories = entry.getValue();

					// 측정 시간 계산
					double measurementTime = monthlyHistories.stream().mapToDouble(HistoryEntity::getMeasuredTime).sum();

					// poseCountMap, poseTimerMap 설정
					Map<PoseStatus, Integer> poseCountMap = new HashMap<>();
					Map<PoseStatus, Long> poseTimerMap = new HashMap<>();

					for (HistoryEntity historyEntity : monthlyHistories) {
						historyEntity.getPoseCountMap().forEach((poseStatus, count) ->
								poseCountMap.merge(poseStatus, count, Integer::sum));
						historyEntity.getPoseTimerMap().forEach((poseStatus, time) ->
								poseTimerMap.merge(poseStatus, time, Long::sum));
					}

					return GetYearDetailResponse.Month.builder()
							.month(month)
							.measurementTime(measurementTime)
							.poseCountMap(poseCountMap)
							.poseTimerMap(poseTimerMap)
							.build();
				})
				.collect(Collectors.toList());

		return GetYearDetailResponse.builder()
				.year(String.valueOf(year))
				.months(months)
				.build();
	}
}
