package com.necklife.api.web.dto.response.member;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PatchProfileResponse {
	private Long id;
	private String nickname;
	private String profile;
}
