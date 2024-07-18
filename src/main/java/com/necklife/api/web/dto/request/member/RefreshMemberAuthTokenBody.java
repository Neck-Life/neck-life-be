package com.necklife.api.web.dto.request.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;


@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class RefreshMemberAuthTokenBody {
	@NotEmpty
	private String refreshToken;
}
