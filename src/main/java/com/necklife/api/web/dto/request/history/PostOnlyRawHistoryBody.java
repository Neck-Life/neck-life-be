package com.necklife.api.web.dto.request.history;

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
public class PostOnlyRawHistoryBody {

	private List<Map<String, String>> rawData;
}
