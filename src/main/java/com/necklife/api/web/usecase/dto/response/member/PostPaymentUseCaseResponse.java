package com.necklife.api.web.usecase.dto.response.member;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PostPaymentUseCaseResponse {

	private String memberId;
	private String status;
	private LocalDateTime serviceEndDate;
}
