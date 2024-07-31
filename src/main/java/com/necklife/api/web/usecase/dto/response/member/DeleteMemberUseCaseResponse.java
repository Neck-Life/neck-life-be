package com.necklife.api.web.usecase.dto.response.member;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class DeleteMemberUseCaseResponse {
	private Long id;
	private LocalDateTime deletedAt;
}
