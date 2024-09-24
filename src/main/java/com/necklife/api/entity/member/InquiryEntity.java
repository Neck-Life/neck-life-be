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
@Document(collection = "inquiry")
public class InquiryEntity {

	@DBRef private MemberEntity member;
	private String title;
	private String content;

	@CreatedDate @NotNull private LocalDateTime createdAt;

	@LastModifiedDate @NotNull private LocalDateTime updatedAt;
}
