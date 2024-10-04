package com.necklife.api.web.usecase.history;

import com.necklife.api.entity.history.PoseStatus;
import com.necklife.api.service.history.SaveHistoryWithRawService;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostRawDataUseCase {

	private final SaveHistoryWithRawService saveHistoryWithRawService;

	private final HashSet<String> rawDataKeySet =
			new HashSet<>(Arrays.asList("timestamp", "position", "status"));

	public void execute(
			String MemberId,
			List<Map<LocalDateTime, String>> historys,
			List<Map<String, String>> rawData) {

		List<TreeMap<LocalDateTime, PoseStatus>> treeMaps = checkHistoryData(historys);

		checkRawData(rawData);

		saveHistoryWithRawService.execute(MemberId, treeMaps, rawData);
	}

	private List<TreeMap<LocalDateTime, PoseStatus>> checkHistoryData(
			List<Map<LocalDateTime, String>> historys) {
		List<TreeMap<LocalDateTime, PoseStatus>> convertedHistoryList = new ArrayList<>();

		for (Map<LocalDateTime, String> subHistory : historys) {

			TreeMap<LocalDateTime, PoseStatus> convertedMap = new TreeMap<>();
			for (Map.Entry<LocalDateTime, String> entry : subHistory.entrySet()) {
				LocalDateTime key = entry.getKey();

				String value = entry.getValue();
				PoseStatus poseStatus;

				try {
					poseStatus = PoseStatus.valueOf(value.toUpperCase());
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException("잘못된 PoseStatus입니다.");
				}

				convertedMap.put(key, poseStatus);
			}
			convertedHistoryList.add(convertedMap);
		}
		return convertedHistoryList;
	}

	private void checkRawData(List<Map<String, String>> rawData) {
		for (Map<String, String> subRawData : rawData) {
			Set<String> strings = subRawData.keySet();

			for (String key : strings) {
				if (!rawDataKeySet.contains(key)) {
					throw new IllegalArgumentException("잘못된 rawData key입니다.");
				}
			}
		}
	}
}
