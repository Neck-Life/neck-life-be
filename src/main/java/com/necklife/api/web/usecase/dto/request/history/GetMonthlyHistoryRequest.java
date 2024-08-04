package com.necklife.api.web.usecase.dto.request.history;

import java.util.Objects;

public record GetMonthlyHistoryRequest(String memberId, Integer year, Integer month) {

	public GetMonthlyHistoryRequest {
		Objects.requireNonNull(memberId);
		Objects.requireNonNull(year);
		Objects.requireNonNull(month);
	}
}
