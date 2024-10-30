package com.necklife.api.service.history;

import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.entity.history.PoseStatus;
import com.necklife.api.entity.history.RawHistoryEntity;
import com.necklife.api.entity.member.MemberEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SaveRawDataService {

	public RawHistoryEntity execute(
			MemberEntity memberEntity,
			HistorySummaryEntity historySummaryEntity,
			List<Map<String, String>> rawData) {

		Map<LocalDateTime, PoseStatus> totalPitchStatusMap =
				historySummaryEntity.getTotalPitchStatusMap();
		Map<LocalDateTime, PoseStatus> totalForwardStatusMap =
				historySummaryEntity.getTotalForwardStatusMap();
		Map<LocalDateTime, PoseStatus> totalTiltStatusMap =
				historySummaryEntity.getTotalTiltStatusMap();

		List<LocalDateTime> forSaveLocalDateTimeList = new ArrayList<>();

		// Combine all LocalDateTime keys
		forSaveLocalDateTimeList.addAll(totalPitchStatusMap.keySet());
		forSaveLocalDateTimeList.addAll(totalForwardStatusMap.keySet());
		forSaveLocalDateTimeList.addAll(totalTiltStatusMap.keySet());

		List<Map<String, String>> filteredRawData = new ArrayList<>();
		List<LocalDateTime> alreadyProcessedTimes =
				new ArrayList<>(); // To track already processed times

		// Iterate through each LocalDateTime key
		for (LocalDateTime localDateTime : forSaveLocalDateTimeList) {
			// Check rawData to find records within ±3 seconds of each localDateTime
			for (Map<String, String> rawEntry : rawData) {
				String timestampStr =
						rawEntry.get("timestamp"); // Assuming rawData contains a timestamp field
				if (timestampStr != null) {
					LocalDateTime rawDateTime =
							LocalDateTime.parse(timestampStr); // Convert rawData timestamp to LocalDateTime

					// Check if the rawDateTime is within ±3 seconds of localDateTime
					if (isWithinRange(rawDateTime, localDateTime, 3)) {
						// Ensure that the rawDateTime has not already been processed
						if (!alreadyProcessedTimes.contains(rawDateTime)) {
							filteredRawData.add(rawEntry);
							alreadyProcessedTimes.add(rawDateTime); // Mark this time as processed
						}
					}
				}
			}
		}

		return RawHistoryEntity.builder()
				.member(memberEntity)
				.date(historySummaryEntity.getDate())
				.rawData(filteredRawData) // Save only the filtered data
				.build();
	}

	/** Helper method to check if two LocalDateTime objects are within a given number of seconds. */
	private boolean isWithinRange(
			LocalDateTime rawDateTime, LocalDateTime referenceDateTime, int seconds) {
		return !rawDateTime.isBefore(referenceDateTime.minusSeconds(seconds))
				&& !rawDateTime.isAfter(referenceDateTime.plusSeconds(seconds));
	}
}
