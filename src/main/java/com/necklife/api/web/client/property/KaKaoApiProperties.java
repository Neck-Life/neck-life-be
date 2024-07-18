package com.necklife.api.web.client.property;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KaKaoApiProperties {

    private final String host;
    private final String adminKey;
    private final String uriToken;
    private final String uriTokenInfo;
    private final String uriMeInfo;
    private final String unlink;
    private final String redirectUri;
    private final String clientId;

    public KaKaoApiProperties(
            @Value("${kakao.host}") String host,
            @Value("${kakao.adminKey}") String adminKey,
            @Value("${kakao.uri.token}") String uriToken,
            @Value("${kakao.uri.token_info}") String uriTokenInfo,
            @Value("${kakao.uri.me_info}") String uriMeInfo,
            @Value("${kakao.uri.unlink}") String unlink,
            @Value("${kakao.redirect_uri}") String redirectUri,
            @Value("${kakao.client_id}") String clientId
    ) {
        this.host = host;
        this.adminKey = adminKey;
        this.uriToken = uriToken;
        this.uriTokenInfo = uriTokenInfo;
        this.uriMeInfo = uriMeInfo;
        this.unlink = unlink;
        this.redirectUri = redirectUri;
        this.clientId = clientId;
    }


}