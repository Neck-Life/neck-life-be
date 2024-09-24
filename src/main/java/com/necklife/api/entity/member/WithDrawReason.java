package com.necklife.api.entity.member;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Document(collection = "withDrawReason")
public class WithDrawReason {

	@DBRef private MemberEntity member;

	private String withDrawReason;

	@CreatedDate @NotNull private LocalDateTime createdAt;

	@LastModifiedDate @NotNull private LocalDateTime updatedAt;
}
