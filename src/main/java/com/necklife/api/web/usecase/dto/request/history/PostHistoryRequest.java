package com.necklife.api.web.usecase.dto.request.history;

import com.necklife.api.entity.history.PoseStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

public record PostHistoryRequest(String memberId, List<TreeMap<LocalDateTime, PoseStatus>> subHistories) {

	public PostHistoryRequest {
		Objects.requireNonNull(memberId);
	}
}
