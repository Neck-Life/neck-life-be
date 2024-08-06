package com.necklife.api.web.dto.request.member;


import lombok.*;

import java.time.LocalDateTime;

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
