package com.necklife.api.entity.member;

import com.necklife.api.entity.BaseEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Document(collection = "member")
@CompoundIndex(name = "member_email_certificatonSubject_unique", def = "{'email': 1, 'oauthProvider': 1, 'deletedAt': 1}", unique = true)
public class MemberEntity extends BaseEntity {



	private Long providerId;

	@NotBlank
	@Size(max = 50)
	@Email
	@NotNull
	private String email;

	@Size(max = 50)
	private String password;

	private boolean isSocial;

	@NotBlank
	@NotNull
	private OauthProvider oauthProvider = OauthProvider.NONE;

	@NotBlank
	@NotNull
	private MemberStatus status;

	private String oauthRefreshToken;

	public MemberEntity withDrawn() {
		this.status = MemberStatus.WITHDRAWN;
		return this;
	}

	public MemberEntity inactive() {
		this.status = MemberStatus.INACTIVE;
		return this;
	}

	public MemberEntity paid() {
		this.status = MemberStatus.PAID;
		return this;
	}

	public MemberEntity unpaid() {
		this.status = MemberStatus.UNPAID;
		return this;
	}

	public void updateRefreshToken(String oauthRefreshToken) {
		this.oauthRefreshToken = oauthRefreshToken;
	}
}
