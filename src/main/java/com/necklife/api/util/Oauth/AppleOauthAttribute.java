package com.necklife.api.util.Oauth;

import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.Getter;
import org.json.JSONObject;

@Getter
public class AppleOauthAttribute implements OauthAttribute {

	private final String tokenResult;
	private final String userId;
	private final String email;
	private final String refreshToken;

	public AppleOauthAttribute(String tokenResult) {
		this.tokenResult = tokenResult;
		try {
			JSONObject jsonObj = new JSONObject(tokenResult);
			// ID TOKEN을 통해 회원 고유 식별자 받기
			String idToken = jsonObj.getString("id_token");
			refreshToken = jsonObj.getString("refresh_token");
			SignedJWT signedJWT = SignedJWT.parse(idToken);
			ReadOnlyJWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

			userId = claimsSet.getStringClaim("sub");
			email = claimsSet.getStringClaim("email");

		} catch (Exception e) {
			throw new RuntimeException("API call failed", e);
		}
	}

	@Override
	public String getEmail() {
		return email;
	}
}
