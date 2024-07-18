package com.necklife.api.entity.member;

public enum OauthProvider {
	GOOGLE,
	APPLE,
	KAKAO,
	NONE;
	;

	public String toString() {
		switch (this) {
			case GOOGLE:
				return "google";
			case APPLE:
				return "APPLE";
			case KAKAO:
				return "KAKAO";
			default:
				return "NONE";
		}
	}
}
