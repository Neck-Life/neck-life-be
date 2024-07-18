package com.necklife.api.service.oauth;

import com.necklife.api.util.Oauth.OauthAttribute;
import com.necklife.api.web.client.member.GoogleMemberClient;
import com.necklife.api.web.client.member.dto.KaKaoMemberData;
import com.necklife.api.web.client.member.dto.SocialMemberData;
import com.necklife.api.web.client.member.dto.SocialMemberToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleOauthService {

    private final GoogleMemberClient googleMemberClient;

    public SocialMemberData execute(String id_token) {
        SocialMemberToken socialMemberToken = googleMemberClient.execute(id_token);
        OauthAttribute googleOauth = new OauthAttribute(socialMemberToken.getSocialToken());

        return new KaKaoMemberData(googleOauth.getEmail());

    }

}
