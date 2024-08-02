package com.necklife.api.repository.member.dto.response;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PostMemberRepoResponse {
	private String id;
	private String email;
	private String provider;
	private String status;
	private boolean isNew;
}
