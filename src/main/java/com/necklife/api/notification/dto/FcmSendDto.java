package com.necklife.api.notification.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FcmSendDto {
	private String token;

	private String title;

	private String body;
}
