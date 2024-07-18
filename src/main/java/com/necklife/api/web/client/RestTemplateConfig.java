package com.necklife.api.web.client;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate(
			@Value("${client.timeout.connect}") int connectTimeout,
			@Value("${client.timeout.read}") int readTimeout,
			@Value("${client.pool.max-connect}") int maxConnectPool,
			@Value("${client.pool.max-connect-per-route}") int maxPerRouteConnectPool) {

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectionRequestTimeout(readTimeout);
		factory.setConnectTimeout(connectTimeout);

		HttpClient client =
				HttpClientBuilder.create()
						.setMaxConnTotal(maxConnectPool)
						.setMaxConnPerRoute(maxPerRouteConnectPool)
						.build();

		// todo

		//        factory.setHttpClient(client);
		return new RestTemplate(new BufferingClientHttpRequestFactory(factory));
	}
}
