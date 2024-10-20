package com.necklife.api.web.dto.request.history;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRawPitchTiltHistoryBody {

	private List<Map<LocalDateTime, String>> pitch;

	private List<Map<String, String>> rawData;

	private List<Map<LocalDateTime, String>> forward;
	private List<Map<LocalDateTime, String>> tilt;
}
