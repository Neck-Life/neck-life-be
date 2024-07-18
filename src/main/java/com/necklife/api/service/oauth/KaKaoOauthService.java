package com.necklife.api.service.oauth;

import com.necklife.api.util.Oauth.OauthAttribute;
import com.necklife.api.web.client.member.KaKaoMemberClient;
import com.necklife.api.web.client.member.dto.KaKaoMemberData;
import com.necklife.api.web.client.member.dto.SocialMemberData;
import com.necklife.api.web.client.member.dto.SocialMemberToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KaKaoOauthService {

    private final KaKaoMemberClient kaKaoMemberClient;

    public SocialMemberData execute(String id_token) {
        SocialMemberToken socialMemberToken = kaKaoMemberClient.execute(id_token);
        OauthAttribute kakaoOauth = new OauthAttribute(socialMemberToken.getSocialToken());

        return new KaKaoMemberData(kakaoOauth.getEmail());

    }

}
