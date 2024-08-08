package com.necklife.api.web.dto.request.member;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PostRefoundPaymentRequest {

	private LocalDateTime date;
	private String reason;
	private Double refoundWon;
}
