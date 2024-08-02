package com.necklife.api.web.usecase.history;

import com.necklife.api.entity.history.HistoryEntity;
import com.necklife.api.entity.history.SubHistoryEntity;
import com.necklife.api.repository.history.HistoryRepository;
import com.necklife.api.web.usecase.dto.request.history.GetYearHistoryRequest;
import com.necklife.api.web.usecase.dto.response.history.GetYearDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetYearDetailUseCase {

	private final HistoryRepository historyRepository;
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public GetYearDetailResponse execute(GetYearHistoryRequest getYearHistoryRequest) {


			Calendar calendar = Calendar.getInstance();
			calendar.set(getYearHistoryRequest.year(), Calendar.JANUARY, 1, 0, 0, 0);
			Date startDate = calendar.getTime();
			calendar.set(Calendar.MONTH, Calendar.DECEMBER);
			calendar.set(Calendar.DAY_OF_MONTH, 31);
			Date endDate = calendar.getTime();

			List<HistoryEntity> historyEntities = historyRepository.findAllByMemberIdAndStartAtBetween(getYearHistoryRequest.memberId(), startDate, endDate);

			Map<String, GetYearDetailResponse.Month.MonthBuilder> monthlyDataMap = new HashMap<>();

			for (HistoryEntity historyEntity : historyEntities) {
				String monthKey = String.format("%04d-%02d", getYearHistoryRequest.year(), historyEntity.getStartAt().getMonth() + 1);
				GetYearDetailResponse.Month.MonthBuilder monthlyDataBuilder = monthlyDataMap.getOrDefault(monthKey, GetYearDetailResponse.Month.builder().month(monthKey));

				List<SubHistoryEntity> subHistories = historyEntity.getSubHistory();

				int measurementTime = (int) ((historyEntity.getEndAt().getTime() - historyEntity.getStartAt().getTime()) / 1000);
				int forwardTime = 0, backwardTime = 0, tiltedTime = 0, normalTime = 0;
				int forwardCount = 0, backwardCount = 0, tiltedCount = 0;

				SubHistoryEntity previousSubHistory = null;

				if (!subHistories.isEmpty()) {
					// 첫 번째 SubHistoryEntity 이전의 시간을 정상 상태로 계산
					SubHistoryEntity firstSubHistory = subHistories.get(0);
					long initialDuration = (firstSubHistory.getChangedAt().getTime() - historyEntity.getStartAt().getTime()) / 1000;
					normalTime += initialDuration;
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
				}

				monthlyDataBuilder
						.MeasurementTime(monthlyDataBuilder.build().getMeasurementTime() + measurementTime)
						.ForwardTime(monthlyDataBuilder.build().getForwardTime() + forwardTime)
						.BackwardTime(monthlyDataBuilder.build().getBackwardTime() + backwardTime)
						.TiltedTime(monthlyDataBuilder.build().getTiltedTime() + tiltedTime)
						.NormalTime(monthlyDataBuilder.build().getNormalTime() + normalTime)
						.ForwardCount(monthlyDataBuilder.build().getForwardCount() + forwardCount)
						.BackwardCount(monthlyDataBuilder.build().getBackwardCount() + backwardCount)
						.TiltedCount(monthlyDataBuilder.build().getTiltedCount() + tiltedCount);

				monthlyDataMap.put(monthKey, monthlyDataBuilder);
			}

			List<GetYearDetailResponse.Month> months = monthlyDataMap.values().stream()
					.map(GetYearDetailResponse.Month.MonthBuilder::build)
					.collect(Collectors.toList());

			return GetYearDetailResponse.builder()
					.year(String.valueOf(getYearHistoryRequest.year()))
					.months(months)
					.build();
		}
	}

