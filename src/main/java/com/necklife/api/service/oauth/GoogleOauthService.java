package com.necklife.api.service.oauth;

import com.necklife.api.util.Oauth.GoogleOauthAttribute;
import com.necklife.api.web.client.member.GoogleMemberClient;
import com.necklife.api.web.client.member.dto.socialData.GoogleMemberData;
import com.necklife.api.web.client.member.dto.socialData.KaKaoMemberData;
import com.necklife.api.web.client.member.dto.socialData.SocialMemberData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleOauthService {

	private final GoogleMemberClient googleMemberClient;

	public SocialMemberData execute(String id_token) {
		String socialTokenResult = googleMemberClient.execute(id_token);
		GoogleOauthAttribute googleOauth = new GoogleOauthAttribute(socialTokenResult);

		return new GoogleMemberData(googleOauth.getEmail());
	}
}
