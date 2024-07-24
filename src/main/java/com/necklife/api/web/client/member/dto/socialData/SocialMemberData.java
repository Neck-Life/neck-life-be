package com.necklife.api.web.client.member.dto.socialData;

import com.necklife.api.entity.member.OauthProvider;

public interface SocialMemberData {

	String getEmail();

	OauthProvider getProvider();

	String getRefreshToken();
}
