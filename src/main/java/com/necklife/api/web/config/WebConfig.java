//package com.necklife.api.web.config;
//
//
//import com.necklife.api.security.config.CorsConfigurationSourceProperties;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.format.FormatterRegistry;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.List;
//
//@Configuration
//@RequiredArgsConstructor
//public class WebConfig implements WebMvcConfigurer {
//
//	private final CorsConfigurationSourceProperties corsProperties;
//
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry
//				.addMapping(corsProperties.getPathPattern())
//				// .allowedOriginPatterns(corsProperties.getOriginPattern())
//				.allowedOrigins(corsProperties.getOriginPatterns())
//				.allowedMethods(corsProperties.getAllowedMethods())
//				.allowedHeaders(corsProperties.getAllowedHeaders())
//				.allowCredentials(corsProperties.getAllowCredentials())
//				.exposedHeaders(corsProperties.getExposedHeaders())
//				.maxAge(corsProperties.getMaxAge());
//	}
//
//	@Override
//	public void addFormatters(FormatterRegistry registry) {
//		registry.addConverter(new OrderFilterConverter());
//	}
//
//	@Override
//	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//		resolvers.add(addOptionalViewPointParamArgumentResolver());
//		resolvers.add(addOptionalTrafficPointParamArgumentResolver());
//	}
//
//	private OptionalViewPointParamArgumentResolver addOptionalViewPointParamArgumentResolver() {
//		return new OptionalViewPointParamArgumentResolver();
//	}
//
//	private OptionalTrafficPointParamArgumentResolver addOptionalTrafficPointParamArgumentResolver() {
//		return new OptionalTrafficPointParamArgumentResolver();
//	}
//}
