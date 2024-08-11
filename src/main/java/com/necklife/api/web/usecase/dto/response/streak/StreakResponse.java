package com.necklife.api.web.usecase.dto.response.streak;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;

@Data
@Builder
@Getter
public class StreakResponse {


    private LocalDate lastGoalStreakDate; // 마지막 목표 달성 스트릭이 기록된 날짜
    private int currentGoalStreak; // 현재 목표 달성 스트릭 길    이
    private int maxGoalStreak; // 최대 목표 달성 스트릭 길이

    private LocalDate lastHistoryStreakDate; // 마지막 히스토리 기록 스트릭이 기록된 날짜
    private int currentHistoryStreak; // 현재 히스토리 기록 스트릭 길이
    private int maxHistoryStreak; // 최대 히스토리 기록 스트릭 길이
}
