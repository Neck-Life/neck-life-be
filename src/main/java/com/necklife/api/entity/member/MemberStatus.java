package com.necklife.api.entity.member;

import lombok.ToString;

@ToString
public enum MemberStatus {
	PAID("결제 회원"),
	UNPAID("미결제 회원"),
	INACTIVE("장기 미이용 회원"),
	WITHDRAWN("탈퇴 회원");

	private final String description;

	MemberStatus(String description) {
		this.description = description;
	}
}
