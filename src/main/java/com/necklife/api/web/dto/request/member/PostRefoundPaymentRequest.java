package com.necklife.api.web.dto.request.member;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PostRefoundPaymentRequest {

    private String reason;
    private Double refoundWon;

}
