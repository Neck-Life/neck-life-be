package com.necklife.api.web.usecase.dto.response.history;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class PostRawHistoryResponse {

	private LocalDate date;
	private List<Map<String, String>> rawData;
}
