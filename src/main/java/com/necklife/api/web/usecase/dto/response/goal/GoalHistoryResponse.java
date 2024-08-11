package com.necklife.api.web.usecase.dto.response.goal;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.TreeMap;

@Data
@Builder
@Getter
public class GoalHistoryResponse{

    private TreeMap<LocalDate, Double> goalHistories;
}
