package com.necklife.api.entity.member;

import com.necklife.api.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Builder
@Table(
		name="member",
		uniqueConstraints={
				@UniqueConstraint(
						name = "member_email_certificatonSubject_unique",
						columnNames = {
								"email",
								"oauth_provider",

						}
				),
		})
@SQLDelete(sql = "UPDATE member SET deleted_At=CURRENT_DATE where id=?")
public class MemberEntity extends BaseEntity {


	private Long providerId;

	/* 소셜로그인과 일반로그인을 지원하기 때문에 이메일이 겹칠 수 있다. */
	@Column(nullable = false, length = 50)
	private String email;

	/* 일반 로그인을 통해 가입한 회원의 비밀번호 */
	@Column(length = 50)
	private String password;


	@Column(nullable = false)
	private boolean isSocial;

	/* 소셜 로그인을 통해 가입한 회원의 인증 주체 */
	@SuppressWarnings("FieldMayBeFinal")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "oauth_provider")
	private OauthProvider oauthProvider = OauthProvider.NONE;

	@SuppressWarnings("FieldMayBeFinal")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MemberStatus status;




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



}
