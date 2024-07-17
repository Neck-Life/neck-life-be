package com.necklife.api.web.dto.request.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PostOauthMemberBody {

	@NotEmpty
	private String code;

	// 구글, 애플, 카카오
	@NotEmpty
	private String provider;
}
