package com.necklife.api.web.usecase.history;

import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.repository.history.HistorySummaryRepository;
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

	public GetHistoryPointResponse execute(String memberId) {
		Map<LocalDate, Integer> historyPointMap = new TreeMap<>();

		List<HistorySummaryEntity> historySummaryEntities =
				historySummaryRepository.findAllByMemberId(memberId);

		for (HistorySummaryEntity historySummaryEntity : historySummaryEntities) {
			historyPointMap.put(
					historySummaryEntity.getDate(), historySummaryEntity.getTotalHistoryPoint());
		}

		return GetHistoryPointResponse.builder().historyPointMap(historyPointMap).build();
	}
}
