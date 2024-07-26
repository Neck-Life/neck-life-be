package com.necklife.api.web.dto.request.history;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PostureHistoryEntry {

	private LocalDateTime startTime;
	private int duration;
	private String status;
}
