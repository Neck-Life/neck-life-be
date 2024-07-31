package com.necklife.api.web.usecase.dto.request.history;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public record PostHistoryRequest(Long memberId, Date startAt,Date endAt, List<PostSubHistoryDto> subHistories) {

    public PostHistoryRequest {
        Objects.requireNonNull(memberId);
        Objects.requireNonNull(startAt);
        Objects.requireNonNull(endAt);

    }

}
