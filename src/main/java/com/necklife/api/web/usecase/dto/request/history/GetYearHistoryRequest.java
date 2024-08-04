package com.necklife.api.web.usecase.dto.request.history;

import java.util.Objects;

public record GetYearHistoryRequest(String memberId, Integer year) {

	public GetYearHistoryRequest {
		Objects.requireNonNull(memberId);
		Objects.requireNonNull(year);
	}
}
