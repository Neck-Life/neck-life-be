package com.necklife.api.web.client.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AppleTokenProperties {

    private final String teamId;
    private final String loginKey;
    private final String clientId;
    private final String redirectUri;
    private final String keyPath;
    private final String oauthUri;

    public AppleTokenProperties(
            @Value("${apple.team-id}") String teamId,
            @Value("${apple.login-key}") String loginKey,
            @Value("${apple.client-id}") String clientId,
            @Value("${apple.redirect-url}") String redirectUri,
            @Value("${apple.key-path}") String keyPath,
            @Value("${apple.oauth-uri}") String oauthUri
    ) {
        this.teamId = teamId;
        this.loginKey = loginKey;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.keyPath = keyPath;
        this.oauthUri = oauthUri;
    }

}
