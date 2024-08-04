package com.necklife.api.web.dto.request.history;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostPostureHistoryBody {

	private LocalDateTime startAt;
	private LocalDateTime endAt;
	private Map<LocalDateTime, String> history;
}
