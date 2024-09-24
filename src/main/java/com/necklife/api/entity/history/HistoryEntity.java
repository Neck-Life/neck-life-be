package com.necklife.api.entity.history;

import com.necklife.api.entity.member.MemberEntity;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Document(collection = "history")
@CompoundIndex(
		name = "unique_history_idx",
		def = "{'start_at': 1, 'end_at': 1, 'member': 1}",
		unique = true)
public class HistoryEntity {

	@Id private String id;

	@DBRef private MemberEntity member;

	@Field("start_at")
	@Indexed
	private LocalDateTime startAt;

	@Field("end_at")
	private LocalDateTime endAt;

	@Indexed private LocalDate date;

	private double measuredTime;

	private Map<LocalDateTime, PoseStatus> poseStatusMap;

	private Map<PoseStatus, Integer> poseCountMap;

	private Map<PoseStatus, Long> poseTimerMap;

	private Integer historyPoint;

	@CreatedDate @NotNull private LocalDateTime createdAt;

	@LastModifiedDate @NotNull private LocalDateTime updatedAt;

	public HistoryEntity updateHistoryPoint() {
		double baseScore =
				100
						* (poseTimerMap.getOrDefault(PoseStatus.NORMAL, 0L) / (double) 60
								+ poseCountMap.getOrDefault(PoseStatus.NORMAL, 0))
						/ (measuredTime / (double) 60
								+ poseCountMap.getOrDefault(PoseStatus.FORWARD, 0)
								+ poseCountMap.getOrDefault(PoseStatus.NORMAL, 0));

		// 점수 상승 계산 (정상 시간이 많을수록 빠르게 상승)
		double scoreIncrease =
				0.2
						* Math.log(
								1
										+ poseTimerMap.getOrDefault(PoseStatus.NORMAL, 0L) / (double) 60
										+ poseCountMap.getOrDefault(PoseStatus.NORMAL, 0));

		// 점수 하락 계산 (거북목 시간이 많을수록 빠르게 하락)
		double scoreDecrease =
				0.2
						* Math.log(
								1
										+ poseTimerMap.getOrDefault(PoseStatus.FORWARD, 0L) / (double) 60
										+ poseCountMap.getOrDefault(PoseStatus.FORWARD, 0));

		// 최종 점수 계산
		double finalScore = baseScore + scoreIncrease - scoreDecrease;

		// 점수는 0~100 사이로 제한
		this.historyPoint = (int) Math.max(0, Math.min(100, finalScore));

		return this;
	}
}
