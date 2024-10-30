package com.necklife.api.web.usecase.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PostMemberUseCaseRequest {

	private String code;
	private String provider;
	private String timeZone;
	private String language;
	private String notificationToken;
}
