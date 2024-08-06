package com.necklife.api.web.usecase.dto.request.history;

import com.necklife.api.entity.history.PoseStatus;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public record PostHistoryRequest(
		String memberId,
		TreeMap<LocalDateTime, PoseStatus> subHistories) {

	public PostHistoryRequest {
		Objects.requireNonNull(memberId);

	}
}
