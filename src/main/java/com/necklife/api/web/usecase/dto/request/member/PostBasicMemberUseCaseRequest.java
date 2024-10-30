package com.necklife.api.web.usecase.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PostBasicMemberUseCaseRequest {

	private String email;
	private String password;

	private String timeZone;
	private String language;
	private String notificationToken;
}
