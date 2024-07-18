package com.necklife.api.service.oauth;

import com.necklife.api.util.Oauth.OauthAttribute;
import com.necklife.api.web.client.member.AppleMemberClient;
import com.necklife.api.web.client.member.dto.AppleMemberData;
import com.necklife.api.web.client.member.dto.SocialMemberData;
import com.necklife.api.web.client.member.dto.SocialMemberToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppleOauthService {

	private final AppleMemberClient appleMemberClient;

	public SocialMemberData execute(String id_token) {
		SocialMemberToken socialMemberToken = appleMemberClient.execute(id_token);
		OauthAttribute appleOauth = new OauthAttribute(socialMemberToken.getSocialToken());

		return new AppleMemberData(appleOauth.getEmail());
	}
}
