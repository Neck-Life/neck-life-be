package com.necklife.api.entity.member;

import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Document(collection = "payment")
public class PaymentEntity {

	@Id private String id;

	@DBRef private MemberEntity member;

	@Field("updated_at")
	private LocalDateTime updatedAt;

	private String status;

	@Field("end_at")
	private LocalDateTime endAt;

	// for payment
	private double paymentWon;

	// for Refound

	private double refoundWon;

	private String Refoundreason;
}
