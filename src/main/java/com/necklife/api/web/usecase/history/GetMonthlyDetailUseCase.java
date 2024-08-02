package com.necklife.api.web.usecase.history;

import com.necklife.api.entity.history.HistoryEntity;
import com.necklife.api.entity.history.PoseStatus;
import com.necklife.api.entity.history.SubHistoryEntity;
import com.necklife.api.repository.history.HistoryRepository;
import com.necklife.api.web.usecase.dto.request.history.GetMonthlyHistoryRequest;
import com.necklife.api.web.usecase.dto.response.history.GetMonthlyDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetMonthlyDetailUseCase {

	private final HistoryRepository historyRepository;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public GetMonthlyDetailResponse execute(GetMonthlyHistoryRequest getMonthlyHistoryRequest) {

		Calendar calendar = Calendar.getInstance();
		calendar.set(getMonthlyHistoryRequest.year(), getMonthlyHistoryRequest.month(), 1, 0, 0, 0);
		Date startDate = calendar.getTime();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date endDate = calendar.getTime();

		List<HistoryEntity> historyEntities = historyRepository.findAllByMemberIdAndStartAtBetween(getMonthlyHistoryRequest.memberId(),startDate, endDate);

		Map<String, GetMonthlyDetailResponse.Day.DayBuilder> dailyDataMap = new HashMap<>();

		for (HistoryEntity historyEntity : historyEntities) {
			String date = dateFormat.format(historyEntity.getStartAt());
			GetMonthlyDetailResponse.Day.DayBuilder dailyDataBuilder = dailyDataMap.getOrDefault(date, GetMonthlyDetailResponse.Day.builder().date(date));

			List<SubHistoryEntity> subHistories = historyEntity.getSubHistory();

			int measurementTime = (int) ((historyEntity.getEndAt().getTime() - historyEntity.getStartAt().getTime()) / 1000);
			int forwardTime = 0, backwardTime = 0, tiltedTime = 0, normalTime = 0;
			int forwardCount = 0, backwardCount = 0, tiltedCount = 0;

			List<GetMonthlyDetailResponse.StatusChange> statusChanges = new ArrayList<>();
			SubHistoryEntity previousSubHistory = null;

			if (!subHistories.isEmpty()) {
				// 첫 번째 SubHistoryEntity 이전의 시간을 정상 상태로 계산
				SubHistoryEntity firstSubHistory = subHistories.get(0);
				long initialDuration = (firstSubHistory.getChangedAt().getTime() - historyEntity.getStartAt().getTime()) / 1000;
				normalTime += initialDuration;

				statusChanges.add(new GetMonthlyDetailResponse.StatusChange(dateFormat.format(historyEntity.getStartAt()), PoseStatus.NORMAL));
			}

			for (SubHistoryEntity subHistory : subHistories) {
				if (previousSubHistory != null) {
					long duration = (subHistory.getChangedAt().getTime() - previousSubHistory.getChangedAt().getTime()) / 1000;

					switch (previousSubHistory.getPoseStatus()) {
						case FORWARD:
							forwardTime += duration;
							forwardCount++;
							break;
						case BACKWARD:
							backwardTime += duration;
							backwardCount++;
							break;
						case TILTED:
							tiltedTime += duration;
							tiltedCount++;
							break;
						case NORMAL:
							normalTime += duration;
							break;
					}

					statusChanges.add(new GetMonthlyDetailResponse.StatusChange(dateFormat.format(previousSubHistory.getChangedAt()), previousSubHistory.getPoseStatus()));
				}
				previousSubHistory = subHistory;
			}

			// 마지막 상태의 지속 시간 계산
			if (previousSubHistory != null) {
				long duration = (historyEntity.getEndAt().getTime() - previousSubHistory.getChangedAt().getTime()) / 1000;
				switch (previousSubHistory.getPoseStatus()) {
					case FORWARD:
						forwardTime += duration;
						forwardCount++;
						break;
					case BACKWARD:
						backwardTime += duration;
						backwardCount++;
						break;
					case TILTED:
						tiltedTime += duration;
						tiltedCount++;
						break;
					case NORMAL:
						normalTime += duration;
						break;
				}

				statusChanges.add(new GetMonthlyDetailResponse.StatusChange(dateFormat.format(previousSubHistory.getChangedAt()), previousSubHistory.getPoseStatus()));
			}

			dailyDataBuilder
					.MeasurementTime(dailyDataBuilder.build().getMeasurementTime() + measurementTime)
					.ForwardTime(dailyDataBuilder.build().getForwardTime() + forwardTime)
					.BackwardTime(dailyDataBuilder.build().getBackwardTime() + backwardTime)
					.TiltedTime(dailyDataBuilder.build().getTiltedTime() + tiltedTime)
					.ForwardCount(dailyDataBuilder.build().getForwardCount() + forwardCount)
					.BackwardCount(dailyDataBuilder.build().getBackwardCount() + backwardCount)
					.TiltedCount(dailyDataBuilder.build().getTiltedCount() + tiltedCount)
					.normalTime(dailyDataBuilder.build().getNormalTime() + normalTime)
					.statusChanges(statusChanges);

			dailyDataMap.put(date, dailyDataBuilder);
		}

		List<GetMonthlyDetailResponse.Day> days = dailyDataMap.values().stream()
				.map(GetMonthlyDetailResponse.Day.DayBuilder::build)
				.collect(Collectors.toList());

		return GetMonthlyDetailResponse.builder()
				.month(getMonthlyHistoryRequest.year() + "-" + getMonthlyHistoryRequest.month())
				.days(days)
				.build();
	}
}
