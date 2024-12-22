package com.necklife.api.web.usecase.goal;

import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.repository.history.RangeHistorySummaryRepository;
import com.necklife.api.web.usecase.dto.response.goal.Recent30StreakDto;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetRecent30StreakUseCase {

	private final RangeHistorySummaryRepository rangeHistorySummaryRepository;

	public List<Recent30StreakDto> execute(String memberId) {

		Recent30StreakDto[] recent30StreakDtos = new Recent30StreakDto[30];

		for (int i = 0; i < recent30StreakDtos.length; i++) {
			recent30StreakDtos[i] = Recent30StreakDto.builder().day(i + 1).point(0).build();
		}

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startDate = now.minusDays(30);
		List<HistorySummaryEntity> findHistorySummaries =
				rangeHistorySummaryRepository.findByTimestampRange(memberId, startDate);

		double maxMeasuredTime =
				findHistorySummaries.stream()
						.mapToDouble(HistorySummaryEntity::getMeasuredTime)
						.max()
						.orElse(1);

		List<HistoryWithDay> collect =
				findHistorySummaries.stream()
						.map(
								HistorySummaryEntity -> {
									// 각 history의 날짜와 30일 전의 날짜 차이 계산하여 일차 추가
									long daysFromStart =
											Duration.between(startDate, HistorySummaryEntity.getDate().atStartOfDay())
													.toDays();
									int dayNumber = (int) daysFromStart + 1; // 1일차부터 시작하도록 조정
									return new HistoryWithDay(HistorySummaryEntity, dayNumber);
								})
						.toList();

		if (!collect.isEmpty() && collect.get(collect.size() - 1).dayNumber == 31) {
			if (collect.get(0).getDayNumber() == 1) {
				collect.remove(0);
			}

			for (HistoryWithDay historyWithDay : collect) {
				historyWithDay.dayNumber = historyWithDay.dayNumber - 1;
			}
		}

		for (HistoryWithDay historyWithDay : collect) {

			int point =
					Math.max(
							(int) (historyWithDay.getHistory().getMeasuredTime() * 100 / maxMeasuredTime), 1);
			recent30StreakDtos[historyWithDay.getDayNumber() - 1] =
					Recent30StreakDto.builder().day(historyWithDay.getDayNumber()).point(point).build();
		}

		List<Recent30StreakDto> list = Arrays.stream(recent30StreakDtos).toList();

		return list;
	}

	// 30일차 필드가 추가된 HistoryWithDay 클래스를 정의

	@Data
	private static class HistoryWithDay {
		private HistorySummaryEntity history;
		private int dayNumber;

		public HistoryWithDay(HistorySummaryEntity history, int dayNumber) {
			this.history = history;
			this.dayNumber = dayNumber;
		}
	}
}
