package com.necklife.api.web.usecase.dto.response.history;

import java.time.LocalDate;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetHistoryPointResponse {

	Map<LocalDate, Integer> historyPointMap;
}
