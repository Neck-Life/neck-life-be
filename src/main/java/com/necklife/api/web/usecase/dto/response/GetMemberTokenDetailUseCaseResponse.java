package com.necklife.api.web.usecase.dto.response;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class GetMemberTokenDetailUseCaseResponse {
	private Long id;
}
