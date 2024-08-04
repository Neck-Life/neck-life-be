package com.necklife.api.service.history;

import com.necklife.api.entity.history.HistoryEntity;
import com.necklife.api.entity.history.PoseStatus;
import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.history.HistoryRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class SaveHistoryService {

	private final HistoryRepository historyRepository;

	public void execute(
			MemberEntity memberEntity,
			LocalDateTime startAt,
			LocalDateTime endAt,
			Map<LocalDateTime, PoseStatus> history) {

		// subHistories 정리하기

		Map<LocalDateTime, PoseStatus> poseStatusMap = new TreeMap<>();
		Map<PoseStatus, Integer> poseCountMap = new HashMap<>();
		Map<PoseStatus, Long> poseTimeMap = new HashMap<>();

		// history 만들기
		HistoryEntity newHistory =
				HistoryEntity.builder()
						.member(memberEntity)
						.measuredTime(ChronoUnit.SECONDS.between(startAt, endAt))
						.startAt(startAt)
						.endAt(endAt)
						.year(startAt.getYear())
						.month(startAt.getMonthValue())
						.poseStatusMap(poseStatusMap)
						.poseCountMap(poseCountMap)
						.poseTimerMap(poseTimeMap)
						.build();

		LocalDateTime beforeStateTime = startAt;
		PoseStatus beforeState = PoseStatus.valueOf("NORMAL");
		for (Map.Entry<LocalDateTime, PoseStatus> subHistory : history.entrySet()) {
			LocalDateTime subHistoryDate = subHistory.getKey();
			PoseStatus subHistoryStatus = subHistory.getValue();

			poseStatusMap.put(subHistoryDate, subHistoryStatus);
			poseCountMap.put(subHistoryStatus, poseCountMap.getOrDefault(subHistoryStatus, 0) + 1);

			long duration = ChronoUnit.SECONDS.between(beforeStateTime, subHistoryDate);
			poseTimeMap.put(
					beforeState, (poseTimeMap.getOrDefault(beforeState, 0L) + (int) duration / 1000));

			beforeState = subHistoryStatus;
			beforeStateTime = subHistoryDate;
		}

		// 마지막은 따로 더하기
		long duration = ChronoUnit.SECONDS.between(beforeStateTime, endAt);
		poseTimeMap.put(beforeState, poseTimeMap.getOrDefault(beforeState, 0L) + (int) duration / 1000);

		// history 저장

		historyRepository.save(newHistory);
	}
}
