package com.necklife.api.web.client.member.dto.socialData;

import com.necklife.api.entity.member.OauthProvider;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppleMemberData implements SocialMemberData {
	private String email;
	private String refreshToken;
	private OauthProvider oauthProvider = OauthProvider.APPLE;

	public AppleMemberData(String email, String refreshToken) {
		this.email = email;
		this.refreshToken = refreshToken;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
	public OauthProvider getProvider() {
		return this.oauthProvider;
	}

	@Override
	public String getRefreshToken() {
		return refreshToken;
	}
}
