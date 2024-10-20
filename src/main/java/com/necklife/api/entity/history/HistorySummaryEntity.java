package com.necklife.api.entity.history;

import com.necklife.api.entity.goal.GoalEntity;
import com.necklife.api.entity.goal.GoalEntity.GoalDetail;
import com.necklife.api.entity.goal.GoalType;
import com.necklife.api.entity.member.MemberEntity;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
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

	@DBRef private GoalEntity goal; // 이 히스토리 요약과 연관된 목표 (특정 버전)

	private Map<GoalType, Double> goalAchievements;

	private double goalAchivedRate;

	private double measuredTime;

	// Deprecated
	private TreeMap<LocalDateTime, PoseStatus> totalPoseStatusMap;

	private Map<PoseStatus, Integer> totalPoseCountMap;

	private Map<PoseStatus, Long> totalPoseTimerMap;

	private Integer totalHistoryPoint;

	// v3부터 적용
	private Map<LocalDateTime, PoseStatus> totalPitchStatusMap;
	private Map<LocalDateTime, PoseStatus> totalForwardStatusMap;
	private Map<LocalDateTime, PoseStatus> totalTiltStatusMap;

	@CreatedDate @NotNull private LocalDateTime createdAt;

	@LastModifiedDate @NotNull private LocalDateTime updatedAt;

	public HistorySummaryEntity updateHistoryPoint() {

		double baseScore =
				100
						* (totalPoseTimerMap.getOrDefault(PoseStatus.NORMAL, 0L) / (double) 60
								+ totalPoseCountMap.getOrDefault(PoseStatus.NORMAL, 0))
						/ (measuredTime / (double) 60
								+ totalPoseCountMap.getOrDefault(PoseStatus.FORWARD, 0)
								+ totalPoseCountMap.getOrDefault(PoseStatus.NORMAL, 0));

		// 점수 상승 계산 (정상 시간이 많을수록 빠르게 상승)
		double scoreIncrease =
				0.2
						* Math.log(
								1
										+ totalPoseTimerMap.getOrDefault(PoseStatus.NORMAL, 0L) / (double) 60
										+ totalPoseCountMap.getOrDefault(PoseStatus.NORMAL, 0));

		// 점수 하락 계산 (거북목 시간이 많을수록 빠르게 하락)
		double scoreDecrease =
				0.2
						* Math.log(
								1
										+ totalPoseTimerMap.getOrDefault(PoseStatus.FORWARD, 0L) / (double) 60
										+ totalPoseCountMap.getOrDefault(PoseStatus.FORWARD, 0));

		// 최종 점수 계산
		double finalScore = baseScore + scoreIncrease - scoreDecrease;

		// 점수는 0~100 사이로 제한
		this.totalHistoryPoint = (int) Math.max(0, Math.min(100, finalScore));

		return this;
	}

	public HistorySummaryEntity updateHistoryPointV3() {

		// 기본 점수 설정 (60점)
		double baseScore = 60;

		// 정상 자세에 따른 점수 상승 계산 (DOWNNORMAL, FORWARDNORMAL, TILTNORMAL 상태 고려)
		double downNormalTime = totalPoseTimerMap.getOrDefault(PoseStatus.DOWNNORMAL, 0L) / (double) 60;
		double forwardNormalTime =
				totalPoseTimerMap.getOrDefault(PoseStatus.FORWARDNORMAL, 0L) / (double) 60;
		double tiltNormalTime = totalPoseTimerMap.getOrDefault(PoseStatus.TILTNORMAL, 0L) / (double) 60;

		// 총 정상 자세 시간 합산
		double normalTime = downNormalTime + forwardNormalTime + tiltNormalTime;

		// 거북목 자세에 따른 점수 하락 계산 (FORWARD, TILT, DOWN 상태 고려)
		double forwardTime = totalPoseTimerMap.getOrDefault(PoseStatus.FORWARD, 0L) / (double) 60;
		double tiltTime = totalPoseTimerMap.getOrDefault(PoseStatus.TILT, 0L) / (double) 60;
		double downTime = totalPoseTimerMap.getOrDefault(PoseStatus.DOWN, 0L) / (double) 60;

		// 총 비정상 자세 시간 합산
		double abnormalTime = forwardTime + tiltTime + downTime;

		// 점수 상승 계산 (정상 자세 시간이 많을수록 점수 상승)
		double scoreIncrease = 0.2 * Math.log(1 + normalTime);

		// 점수 하락 계산 (비정상 자세 시간이 많을수록 점수 하락)
		double scoreDecrease = 0.2 * Math.log(1 + abnormalTime);

		// 최종 점수 계산
		double finalScore = baseScore + scoreIncrease - scoreDecrease;

		// 점수는 0~100 사이로 제한
		this.totalHistoryPoint = (int) Math.max(0, Math.min(100, finalScore));

		return this;
	}

	// deprecated
	public HistorySummaryEntity updateSummary(
			TreeMap<LocalDateTime, PoseStatus> totalPoseStatusMap,
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

			totalPoseCountMap.forEach(
					((poseStatus, count) -> this.totalPoseCountMap.merge(poseStatus, count, Integer::sum)));
		}

		if (this.totalPoseTimerMap == null) {
			this.totalPoseTimerMap = totalPoseTimerMap;
		} else {

			totalPoseTimerMap.forEach(
					((poseStatus, count) -> this.totalPoseTimerMap.merge(poseStatus, count, Long::sum)));
		}

		return this;
	}

	public HistorySummaryEntity updateSummary(
			TreeMap<LocalDateTime, PoseStatus> totalPitchStatusMap,
			TreeMap<LocalDateTime, PoseStatus> totalForwardStatusMap,
			TreeMap<LocalDateTime, PoseStatus> totalTiltStatusMap,
			Map<PoseStatus, Integer> totalPoseCountMap,
			Map<PoseStatus, Long> totalPoseTimerMap) {

		if (this.totalPitchStatusMap == null) {
			this.totalPitchStatusMap = totalPitchStatusMap;
		} else {
			this.totalPitchStatusMap.putAll(totalPitchStatusMap);
		}

		if (this.totalForwardStatusMap == null) {
			this.totalForwardStatusMap = totalForwardStatusMap;
		} else {
			this.totalForwardStatusMap.putAll(totalForwardStatusMap);
		}

		if (this.totalTiltStatusMap == null) {
			this.totalTiltStatusMap = totalTiltStatusMap;
		} else {
			this.totalTiltStatusMap.putAll(totalTiltStatusMap);
		}

		if (this.totalPoseCountMap == null) {
			this.totalPoseCountMap = totalPoseCountMap;
		} else {

			totalPoseCountMap.forEach(
					((poseStatus, count) -> this.totalPoseCountMap.merge(poseStatus, count, Integer::sum)));
		}

		if (this.totalPoseTimerMap == null) {
			this.totalPoseTimerMap = totalPoseTimerMap;
		} else {

			totalPoseTimerMap.forEach(
					((poseStatus, count) -> this.totalPoseTimerMap.merge(poseStatus, count, Long::sum)));
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

	public void changeGoals(GoalEntity goal) {
		this.goal = goal;
		// todo
	}

	public void calculateAchievements() {
		// 초기화
		goalAchievements = new HashMap<>();

		if (goal == null || goal.getGoals().isEmpty()) {
			return;
		}

		// GoalEntity에 있는 각 GoalDetail 순회
		for (GoalDetail goalDetail : goal.getGoals()) {
			GoalType type = goalDetail.getType();
			double achievedRate = 0;

			// 목표 유형에 따라 달성 여부 계산
			switch (type) {
				case MEASUREMENT:
					achievedRate = Math.min(measuredTime / goalDetail.getTargetValue(), 1);
					break;
				case SCORE:
					achievedRate = Math.min(totalHistoryPoint / goalDetail.getTargetValue(), 1);
					break;
					// 다른 목표 유형에 대한 처리 추가 가능
			}

			// 결과를 goalAchievements 맵에 저장
			goalAchievements.put(type, achievedRate);
		}
	}
}
