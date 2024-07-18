package com.necklife.api.web.client.util;

import org.springframework.http.HttpHeaders;

public class HeadersFunction {

	public static void addBearerHeader(HttpHeaders headers, String token) {
		headers.add("Authorization", "Bearer " + token);
	}

	public static void addKakaoHeader(HttpHeaders headers, String adminKey) {
		headers.add("Authorization", "KakaoAK " + adminKey);
	}
}
