package com.necklife.api.web.usecase.dto.request.history;

import java.util.Objects;

public record GetYearHistoryRequest(Long memberId, Integer year) {

    public GetYearHistoryRequest {
        Objects.requireNonNull(memberId);
        Objects.requireNonNull(year);

    }
}
