package com.necklife.api.web.dto.response.member;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberTokenResponse {

	private String accessToken;
	private String refreshToken;
}
