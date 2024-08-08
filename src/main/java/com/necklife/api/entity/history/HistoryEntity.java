package com.necklife.api.entity.history;

import com.necklife.api.entity.member.MemberEntity;
import java.time.LocalDateTime;
import java.util.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Document(collection = "history")
public class HistoryEntity {

	@Id private String id;

	@DBRef private MemberEntity member;

	@Field("start_at")
	@Indexed
	private LocalDateTime startAt;

	@Field("end_at")
	private LocalDateTime endAt;

	private int year;
	private int month;

	private double measuredTime;

	private Map<LocalDateTime, PoseStatus> poseStatusMap;

	private Map<PoseStatus, Integer> poseCountMap;

	private Map<PoseStatus, Long> poseTimerMap;
}
