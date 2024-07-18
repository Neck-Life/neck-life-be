package com.necklife.api.entity;

import com.necklife.api.entity.listener.SoftDeleteListener;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, SoftDeleteListener.class})
public abstract class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, updatable = false)
	@CreatedDate
	private LocalDateTime createdAt;

	@Column(nullable = false)
	@LastModifiedDate
	private LocalDateTime updatedAt;

	private LocalDateTime deletedAt;

	public void delete() {
		this.deletedAt = LocalDateTime.now();
	}
}
