package com.necklife.api.entity;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Document
public abstract class BaseEntity {

	@MongoId(FieldType.OBJECT_ID)
	private String id;

	@CreatedDate
	@NotNull(message = "User's first name must not be null")
	private LocalDateTime createdAt;

	@LastModifiedDate @NotNull private LocalDateTime updatedAt;

	private LocalDateTime deletedAt;

	public void delete() {
		this.deletedAt = LocalDateTime.now();
	}
}
