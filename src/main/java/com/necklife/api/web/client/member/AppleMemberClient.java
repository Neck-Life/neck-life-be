package com.necklife.api.web.client.member;

import com.necklife.api.web.client.exception.SocialClientException;
import com.necklife.api.web.client.member.dto.SocialMemberToken;
import com.necklife.api.web.client.property.AppleTokenProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleMemberClient implements SocialMemberClient {

	private final RestTemplate restTemplate;
	private final AppleTokenProperties appleTokenProperties;

	@Override
	public SocialMemberToken execute(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded");

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", appleTokenProperties.getClientId());
		params.add("client_secret", createClientSecret());
		params.add("code", code);
		params.add("redirect_uri", appleTokenProperties.getRedirectUri());

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

		ResponseEntity<SocialMemberToken> response =
				restTemplate.exchange(
						appleTokenProperties.getOauthUri() + "/auth/token",
						HttpMethod.POST,
						httpEntity,
						SocialMemberToken.class);

		if (response.getStatusCode().is4xxClientError()) {
			throw new SocialClientException();
		}

		return response.getBody();
	}

	private String createClientSecret() {

		JWSHeader header =
				new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(appleTokenProperties.getLoginKey()).build();
		JWTClaimsSet claimsSet = new JWTClaimsSet();
		Date now = new Date();

		claimsSet.setIssuer(appleTokenProperties.getTeamId());
		claimsSet.setIssueTime(now);
		claimsSet.setExpirationTime(new Date(now.getTime() + 3600000));
		claimsSet.setAudience(appleTokenProperties.getOauthUri() + "/auth/token");
		claimsSet.setSubject(appleTokenProperties.getClientId());

		SignedJWT jwt = new SignedJWT(header, claimsSet);

		PKCS8EncodedKeySpec spec =
				new PKCS8EncodedKeySpec(readPrivateKey(appleTokenProperties.getKeyPath()));
		try {
			KeyFactory kf = KeyFactory.getInstance("EC");
			ECPrivateKey ecPrivateKey = (ECPrivateKey) kf.generatePrivate(spec);
			JWSSigner jwsSigner = new ECDSASigner(ecPrivateKey.getD());
			jwt.sign(jwsSigner);
		} catch (JOSEException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}

		return jwt.serialize();
	}

	/**
	 * 파일에서 private key 획득
	 *
	 * @return Private Key
	 */
	private static byte[] readPrivateKey(String keyPath) {

		Resource resource = new ClassPathResource(keyPath);
		byte[] content = null;

		try (InputStream keyInputStream = resource.getInputStream();
				InputStreamReader keyReader = new InputStreamReader(keyInputStream);
				PemReader pemReader = new PemReader(keyReader)) {
			PemObject pemObject = pemReader.readPemObject();
			content = pemObject.getContent();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}
}
