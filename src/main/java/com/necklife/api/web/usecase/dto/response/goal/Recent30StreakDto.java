package com.necklife.api.web.usecase.dto.response.goal;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class Recent30StreakDto {

	private Integer day;
	private Integer point;
}
