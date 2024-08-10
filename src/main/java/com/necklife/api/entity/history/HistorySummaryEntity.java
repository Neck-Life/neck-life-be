package com.necklife.api.entity.history;

import com.necklife.api.entity.member.MemberEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Document(collection = "history_summary")
public class HistorySummaryEntity {

	@Id private String id;

	@DBRef private MemberEntity member;

	@Indexed private LocalDate date;

	private double measuredTime;

	private Map<LocalDateTime, PoseStatus> totalPoseStatusMap;

	private Map<PoseStatus, Integer> totalPoseCountMap;

	private Map<PoseStatus, Long> totalPoseTimerMap;

	private Integer totalHistoryPoint;

	public HistorySummaryEntity updateHistoryPoint(Integer historyPoint) {
		if (this.totalHistoryPoint == null) {
			this.totalHistoryPoint = historyPoint;
		} else {
			this.totalHistoryPoint += historyPoint;
		}

		return this;
	}

	public HistorySummaryEntity updateSummary(
			Map<LocalDateTime, PoseStatus> totalPoseStatusMap,
			Map<PoseStatus, Integer> totalPoseCountMap,
			Map<PoseStatus, Long> totalPoseTimerMap) {
		if (this.totalPoseStatusMap == null) {
			this.totalPoseStatusMap = totalPoseStatusMap;
		} else {
			this.totalPoseStatusMap.putAll(totalPoseStatusMap);
		}

		if (this.totalPoseCountMap == null) {
			this.totalPoseCountMap = totalPoseCountMap;
		} else {
			this.totalPoseCountMap.putAll(totalPoseCountMap);
		}

		if (this.totalPoseTimerMap == null) {
			this.totalPoseTimerMap = totalPoseTimerMap;
		} else {
			this.totalPoseTimerMap.putAll(totalPoseTimerMap);
		}

		return this;
	}

	public void updateMeasuredTime(double measuredTime) {
		if (this.measuredTime == 0) {
			this.measuredTime = measuredTime;
		} else {
			this.measuredTime += measuredTime;
		}
	}
}
