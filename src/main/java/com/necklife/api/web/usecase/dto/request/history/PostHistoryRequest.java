package com.necklife.api.web.usecase.dto.request.history;

import com.necklife.api.entity.history.PoseStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public record PostHistoryRequest(String memberId, LocalDateTime startAt, LocalDateTime endAt, Map<LocalDateTime, PoseStatus> subHistories) {

    public PostHistoryRequest {
        Objects.requireNonNull(memberId);
        Objects.requireNonNull(startAt);
        Objects.requireNonNull(endAt);

    }

}
