package com.necklife.api.web.config;

import com.necklife.api.security.config.CorsConfigurationSourceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@RequiredArgsConstructor
@EnableWebMvc
// @EnableAutoConfiguration(
//		exclude = {
//				WebMvcAutoConfiguration.class,
//				ErrorMvcAutoConfiguration.class,
//		})
public class WebConfig extends WebMvcConfigurationSupport {

	private final CorsConfigurationSourceProperties corsProperties;

	//	@Primary
	//	@Bean
	//	public HandlerExceptionResolver handlerExceptionResolver(
	//			@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
	//		return handlerExceptionResolver;
	//	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
				.addMapping(corsProperties.getPathPattern())
				//				.allowedOrigins(corsProperties.getOriginPatterns())
				.allowedMethods(corsProperties.getAllowedMethods())
				.allowedHeaders(corsProperties.getAllowedHeaders())
				//				.allowCredentials(corsProperties.getAllowCredentials())
				.exposedHeaders(corsProperties.getExposedHeaders())
				.maxAge(corsProperties.getMaxAge());
	}
}
