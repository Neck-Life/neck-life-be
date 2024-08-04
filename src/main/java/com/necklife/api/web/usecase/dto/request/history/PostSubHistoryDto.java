package com.necklife.api.web.usecase.dto.request.history;

import com.necklife.api.entity.history.PoseStatus;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostSubHistoryDto {

	private Date changedAt;
	private PoseStatus poseStatus;
}
