package com.necklife.api.web.client.member;

import com.necklife.api.web.client.exception.SocialClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleMemberClient implements SocialMemberClient {

	private final RestTemplate restTemplate;

	@Override
	public String execute(String targetId) {
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<>(headers);
		String googleApi = "https://oauth2.googleapis.com/tokeninfo";
		String targetUrl =
				UriComponentsBuilder.fromHttpUrl(googleApi)
						.queryParam("id_token", targetId)
						//						.queryParam("access_type", "offline")
						.build()
						.toUriString();

		ResponseEntity<String> response =
				restTemplate.exchange(targetUrl, HttpMethod.GET, entity, String.class);

		if (response.getStatusCode().is4xxClientError()) {
			throw new SocialClientException();
		}

		return response.getBody();
	}
}
