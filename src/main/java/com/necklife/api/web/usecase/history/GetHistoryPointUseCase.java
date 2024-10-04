package com.necklife.api.web.usecase.history;

import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.repository.history.HistorySummaryRepository;
import com.necklife.api.web.dto.request.history.HistoryPointEnum;
import com.necklife.api.web.usecase.dto.response.history.GetHistoryPointResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetHistoryPointUseCase {

	private final HistorySummaryRepository historySummaryRepository;

	public GetHistoryPointResponse execute(String memberId, HistoryPointEnum historyPointEnum) {
		Map<LocalDate, Integer> historyPointMap = new TreeMap<>();
		LocalDate now = LocalDate.now();
		Integer beforeDay = historyPointEnum.covertToDateBefore();
		LocalDate startAt = now.minusDays(beforeDay);

		List<HistorySummaryEntity> historySummaryEntities =
				historySummaryRepository.findPointByMemberIdAndDate(memberId, startAt, now);

		for (HistorySummaryEntity historySummaryEntity : historySummaryEntities) {
			historyPointMap.put(
					historySummaryEntity.getDate(), historySummaryEntity.getTotalHistoryPoint());
		}

		return GetHistoryPointResponse.builder().historyPointMap(historyPointMap).build();
	}
}
