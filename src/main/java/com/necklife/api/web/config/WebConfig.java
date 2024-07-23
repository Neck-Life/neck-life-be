package com.necklife.api.web.config;

import com.necklife.api.security.config.CorsConfigurationSourceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@RequiredArgsConstructor
// @EnableAutoConfiguration(
//		exclude = {
//				WebMvcAutoConfiguration.class,
//				ErrorMvcAutoConfiguration.class,
//		})
public class WebConfig extends WebMvcConfigurationSupport {

	private final CorsConfigurationSourceProperties corsProperties;

	@Primary
	@Bean
	public HandlerExceptionResolver handlerExceptionResolver(
			@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
		return handlerExceptionResolver;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
				.addMapping(("/**"))
				.allowedOrigins("*")
				.allowedMethods(
						HttpMethod.GET.name(),
						HttpMethod.HEAD.name(),
						HttpMethod.POST.name(),
						HttpMethod.PUT.name(),
						HttpMethod.DELETE.name());
	}
}
