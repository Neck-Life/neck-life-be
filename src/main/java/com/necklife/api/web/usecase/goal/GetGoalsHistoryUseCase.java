package com.necklife.api.web.usecase.goal;

import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.repository.history.HistorySummaryRepository;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.web.usecase.dto.response.goal.GoalHistoryResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetGoalsHistoryUseCase {

	private final HistorySummaryRepository historySummaryRepository;
	private final MemberRepository memberRepository;

	public GoalHistoryResponse execute(String memberId) {

		if (!memberRepository.existsById(memberId)) {
			throw new IllegalArgumentException("Member not found");
		}

		List<HistorySummaryEntity> allHistory = historySummaryRepository.findAllByMemberId(memberId);

		TreeMap<LocalDate, Double> goalHistoryMap = new TreeMap<>();

		for (HistorySummaryEntity historySummaryEntity : allHistory) {
			goalHistoryMap.put(historySummaryEntity.getDate(), historySummaryEntity.getGoalAchivedRate());
		}

		return GoalHistoryResponse.builder().goalHistories(goalHistoryMap).build();
	}
}
