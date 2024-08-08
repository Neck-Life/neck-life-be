package com.necklife.api.web.dto.request.member;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PostPaymentRequest {

	private LocalDateTime date;
	private Long months;
	private Double won;
}
