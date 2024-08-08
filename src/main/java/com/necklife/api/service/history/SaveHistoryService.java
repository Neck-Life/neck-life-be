package com.necklife.api.service.history;

import com.necklife.api.entity.history.HistoryEntity;
import com.necklife.api.entity.history.PoseStatus;
import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.history.HistoryRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.necklife.api.web.exception.NotSupportHistoryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class SaveHistoryService {

	private final HistoryRepository historyRepository;

	@Transactional
	public void execute(MemberEntity memberEntity, List<TreeMap<LocalDateTime, PoseStatus>> history) {

		List<HistoryEntity> forSaveHistoryEntity = new ArrayList<>();

		// subHistories 정리하기
		for (TreeMap<LocalDateTime, PoseStatus> subHistory : history) {
			Map<LocalDateTime, PoseStatus> poseStatusMap = new TreeMap<>();
			Map<PoseStatus, Integer> poseCountMap = new HashMap<>();
			Map<PoseStatus, Long> poseTimeMap = new HashMap<>();

			LocalDateTime startAt = subHistory.firstKey();
			LocalDateTime endAt = subHistory.lastKey();

			if (subHistory.get(startAt) != PoseStatus.valueOf("START") || subHistory.get(endAt) != PoseStatus.valueOf("END")) {

				throw new NotSupportHistoryException();
			}

			subHistory.remove(startAt);
			subHistory.remove(endAt);

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
			for (Map.Entry<LocalDateTime, PoseStatus> neckEvent : subHistory.entrySet()) {
				LocalDateTime neckEventDate = neckEvent.getKey();
				PoseStatus neckEventStatus = neckEvent.getValue();

				poseStatusMap.put(neckEventDate, neckEventStatus);
				poseCountMap.put(neckEventStatus, poseCountMap.getOrDefault(neckEventStatus, 0) + 1);

				long duration = ChronoUnit.SECONDS.between(beforeStateTime, neckEventDate);
				poseTimeMap.put(
						beforeState, (poseTimeMap.getOrDefault(beforeState, 0L) + (int) duration / 1000));

				beforeState = neckEventStatus;
				beforeStateTime = neckEventDate;
			}

			// 마지막은 따로 더하기
			long duration = ChronoUnit.SECONDS.between(beforeStateTime, endAt);
			poseTimeMap.put(beforeState, poseTimeMap.getOrDefault(beforeState, 0L) + (int) duration / 1000);

			// history 저장

			forSaveHistoryEntity.add(newHistory);
		}

		historyRepository.saveAll(forSaveHistoryEntity);



	}
}
