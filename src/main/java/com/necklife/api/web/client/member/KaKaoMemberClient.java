package com.necklife.api.web.client.member;

import com.necklife.api.web.client.exception.SocialClientException;
import com.necklife.api.web.client.member.dto.SocialMemberToken;
import com.necklife.api.web.client.property.KaKaoApiProperties;
import com.necklife.api.web.client.util.HeadersFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class KaKaoMemberClient implements SocialMemberClient {

	private final RestTemplate restTemplate;
	private final KaKaoApiProperties properties;

	@Override
	public SocialMemberToken execute(String targetId) {
		String adminKey = properties.getAdminKey();

		HttpHeaders headers = new HttpHeaders();
		HeadersFunction.addKakaoHeader(headers, adminKey);
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

		String queryParameter = "?target_id_type=user_id&target_id=" + targetId;
		ResponseEntity<SocialMemberToken> response =
				restTemplate.exchange(
						properties.getUriMeInfo() + queryParameter,
						HttpMethod.POST,
						new HttpEntity<>(null, headers),
						SocialMemberToken.class);

		if (response.getStatusCode().is4xxClientError()) {
			throw new SocialClientException();
		}

		return response.getBody();
	}
}
