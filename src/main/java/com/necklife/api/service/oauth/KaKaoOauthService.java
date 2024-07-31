package com.necklife.api.service.oauth;

import com.necklife.api.util.Oauth.KakaoOauthAttribute;
import com.necklife.api.web.client.member.KaKaoMemberClient;
import com.necklife.api.web.client.member.dto.socialData.KaKaoMemberData;
import com.necklife.api.web.client.member.dto.socialData.SocialMemberData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KaKaoOauthService {

	private final KaKaoMemberClient kaKaoMemberClient;

	public SocialMemberData execute(String id_token) {
		String tokenResult = kaKaoMemberClient.execute(id_token);
		KakaoOauthAttribute kakaoOauth = new KakaoOauthAttribute(tokenResult);

		return new KaKaoMemberData(kakaoOauth.getEmail());
	}
}
