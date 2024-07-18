package com.necklife.api.repository.member.dto.response;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PostMemberRepoResponse {
	private Long id;
	private String email;
	private String provider;
	private String status;
}
