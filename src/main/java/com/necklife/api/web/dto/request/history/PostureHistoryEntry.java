package com.necklife.api.web.dto.request.history;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PostureHistoryEntry {

	private Date startAt;
	private String status;
}
