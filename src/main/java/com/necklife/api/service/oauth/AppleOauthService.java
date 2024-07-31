package com.necklife.api.service.oauth;

import com.necklife.api.service.oauth.util.AppleOauthAttribute;
import com.necklife.api.web.client.member.AppleMemberClient;
import com.necklife.api.web.client.member.dto.socialData.AppleMemberData;
import com.necklife.api.web.client.member.dto.socialData.SocialMemberData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppleOauthService {

	private final AppleMemberClient appleMemberClient;

	public SocialMemberData execute(String id_token) {
		String socialTokenResult = appleMemberClient.execute(id_token);
		AppleOauthAttribute appleOauth = new AppleOauthAttribute(socialTokenResult);

		return new AppleMemberData(appleOauth.getEmail(), appleOauth.getRefreshToken());
	}
}
