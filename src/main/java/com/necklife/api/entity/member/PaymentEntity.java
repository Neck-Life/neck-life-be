package com.necklife.api.entity.member;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Document(collection = "payment")
public class PaymentEntity {

    @Id
    private String id;

    @DBRef
    private MemberEntity member;

    @Field("payment_at")
    private LocalDateTime paymentAt;

    @Field("refound_at")
    private LocalDateTime refoundAt;


    @Field("end_at")
    private LocalDateTime endAt;

    private double paymentWon;

    private double refoundWon;

    private String Refoundreason;

}
