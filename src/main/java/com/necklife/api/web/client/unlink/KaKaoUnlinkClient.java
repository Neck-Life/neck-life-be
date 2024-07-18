package com.necklife.api.web.client.unlink;

import com.necklife.api.web.client.exception.SocialClientException;
import com.necklife.api.web.client.property.KaKaoApiProperties;
import com.necklife.api.web.client.unlink.dto.KaKaoUnlinkData;
import com.necklife.api.web.client.unlink.dto.SocialUnlinkData;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class KaKaoUnlinkClient implements SocialUnlinkClient {

	private final RestTemplate restTemplate;
	private final KaKaoApiProperties properties;

	public KaKaoUnlinkClient(RestTemplate restTemplate, KaKaoApiProperties properties) {
		this.restTemplate = restTemplate;
		this.properties = properties;
	}

	@Override
	public SocialUnlinkData execute(String targetId) {
		String adminKey = properties.getAdminKey();

		HttpHeaders headers = new org.springframework.http.HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
		headers.add(HttpHeaders.AUTHORIZATION, "KakaoAK " + adminKey);

		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("target_id_type", "user_id");
		params.add("target_id", Long.parseLong(targetId));

		ResponseEntity<KaKaoUnlinkData> response;
		try {
			response =
					restTemplate.exchange(
							properties.getUnlink(),
							HttpMethod.POST,
							new HttpEntity<>(params, headers),
							KaKaoUnlinkData.class);
		} catch (RestClientException e) {
			throw new SocialClientException();
		}

		if (response.getStatusCode().is4xxClientError()) {
			throw new SocialClientException();
		}

		return response.getBody();
	}

	@Override
	public boolean supports(String type) {
		return "KAKAO".equalsIgnoreCase(type);
	}
}
