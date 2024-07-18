package com.necklife.api.web.client.member.dto;

import com.necklife.api.entity.member.OauthProvider;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleMemberData implements SocialMemberData {
	private String email;
	private OauthProvider oauthProvider = OauthProvider.GOOGLE;

	public GoogleMemberData(String email) {
		this.email = email;
	}

	@Override
	public String getEmail() {
		return this.email;
	}

	@Override
	public OauthProvider getProvider() {
		return this.oauthProvider;
	}
}
