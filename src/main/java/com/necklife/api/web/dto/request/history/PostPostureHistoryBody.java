package com.necklife.api.web.dto.request.history;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostPostureHistoryBody {

	private Date startAt;
	private Date endAt;
	private List<PostureHistoryEntry> history;
}
