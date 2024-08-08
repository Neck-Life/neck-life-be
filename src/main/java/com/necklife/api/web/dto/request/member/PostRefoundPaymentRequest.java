package com.necklife.api.web.dto.request.member;

import lombok.*;

import java.time.LocalDateTime;

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
