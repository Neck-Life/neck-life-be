package com.necklife.api.web.dto.response.member;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class DeleteMemberResponse {

	private Long id;
	private LocalDateTime deletedAt;
}
