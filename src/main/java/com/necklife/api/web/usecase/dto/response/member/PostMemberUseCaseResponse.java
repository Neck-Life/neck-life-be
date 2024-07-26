package com.necklife.api.web.usecase.dto.response.member;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PostMemberUseCaseResponse {
	private Long id;
	private String email;
	private String provider;
	private String status;
}
