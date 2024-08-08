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
public class PostPostureHistoryBody {

	private List<Map<LocalDateTime, String>> historys;



}
