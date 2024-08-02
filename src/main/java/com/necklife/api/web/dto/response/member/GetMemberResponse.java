package com.necklife.api.web.dto.response.member;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class GetMemberResponse {

	private String id;
	private String email;
	private String provider;
	private String status;
}
