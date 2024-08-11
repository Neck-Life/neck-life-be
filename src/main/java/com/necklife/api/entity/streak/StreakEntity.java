package com.necklife.api.entity.streak;

import com.necklife.api.entity.member.MemberEntity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "streaks")
public class StreakEntity {

    @Id
    private String id;

    @DBRef
    private MemberEntity member; // 스트릭을 추적할 회원

    @Indexed
    private LocalDate lastGoalStreakDate; // 마지막 목표 달성 스트릭이 기록된 날짜
    private int currentGoalStreak; // 현재 목표 달성 스트릭 길    이
    private int maxGoalStreak; // 최대 목표 달성 스트릭 길이

    @Indexed
    private LocalDate lastHistoryStreakDate; // 마지막 히스토리 기록 스트릭이 기록된 날짜
    private int currentHistoryStreak; // 현재 히스토리 기록 스트릭 길이
    private int maxHistoryStreak; // 최대 히스토리 기록 스트릭 길이

}