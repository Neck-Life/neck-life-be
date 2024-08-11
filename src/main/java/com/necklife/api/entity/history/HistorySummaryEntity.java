package com.necklife.api.entity.history;

import com.necklife.api.entity.goal.GoalEntity;
import com.necklife.api.entity.goal.GoalEntity.GoalDetail;
import com.necklife.api.entity.goal.GoalType;
import com.necklife.api.entity.member.MemberEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

	@DBRef
	private GoalEntity goal; // 이 히스토리 요약과 연관된 목표 (특정 버전)

	private Map<GoalType, Boolean> goalAchievements;

	private double goalAchivedRate;

	private double measuredTime;

	private TreeMap<LocalDateTime, PoseStatus> totalPoseStatusMap;

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


	public void changeGoals(GoalEntity goal) {
		this.goal = goal;
		//todo
	}

	public void calculateAchievements() {
		// 초기화
		goalAchievements = new HashMap<>();
		goalAchivedRate = 0;

		if (goal == null || goal.getGoals().isEmpty()) {
			return;
		}

		// GoalEntity에 있는 각 GoalDetail 순회
		for (GoalDetail goalDetail : goal.getGoals()) {
			GoalType type = goalDetail.getType();
			boolean achieved = false;

			// 목표 유형에 따라 달성 여부 계산
			switch (type) {
				case MEASUREMENT:
					achieved = measuredTime >= goalDetail.getTargetValue();
					break;
//				case SCORE:
//					achieved = score >= goalDetail.getTargetValue();
//					break;
//				// 다른 목표 유형에 대한 처리 추가 가능
			}

			// 결과를 goalAchievements 맵에 저장
			goalAchievements.put(type, achieved);
			goalAchivedRate += achieved ? 1 : 0;
		}
		goalAchivedRate/=goal.getGoals().size();
	}

}
