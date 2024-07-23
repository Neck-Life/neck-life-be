package com.necklife.api.security.config;

import com.necklife.api.security.authentication.token.TokenAuthProvider;
import com.necklife.api.security.filter.exception.TokenInvalidExceptionHandlerFilter;
import com.necklife.api.security.filter.token.TokenAuthenticationFilter;
import com.necklife.api.security.handler.DelegatedAccessDeniedHandler;
import com.necklife.api.security.handler.DelegatedAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

	private final DelegatedAuthenticationEntryPoint authenticationEntryPoint;
	private final DelegatedAccessDeniedHandler accessDeniedHandler;
	private final TokenAuthProvider tokenAuthProvider;
	private final CorsConfigurationSourceProperties corsProperties;

	@Bean
	@Profile("!prod")
	public SecurityFilterChain localSecurityFilterChain(HttpSecurity http) throws Exception {

		http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(
						authorize ->
								authorize.requestMatchers("/api/v1/**").authenticated().anyRequest().permitAll())
				.addFilterBefore(
						getTokenInvalidExceptionHandlerFilter(), AbstractPreAuthenticatedProcessingFilter.class)
				.addFilterAt(generateAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
				.exceptionHandling(
						exceptionHandling ->
								exceptionHandling
										.authenticationEntryPoint(authenticationEntryPoint)
										.accessDeniedHandler(accessDeniedHandler))
				.sessionManagement(
						sessionManagement ->
								sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

	@Bean
	@Profile(value = "prod")
	public SecurityFilterChain prodSecurityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.cors(cors -> cors.configurationSource(corsConfigurationSource()));

		http.authorizeHttpRequests(
				authorize ->
						authorize.requestMatchers("/api/v1/**").authenticated().anyRequest().denyAll());

		http.addFilterBefore(
						getTokenInvalidExceptionHandlerFilter(), AbstractPreAuthenticatedProcessingFilter.class)
				.addFilterAt(
						generateAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class);

		http.exceptionHandling(
				exceptionHandling ->
						exceptionHandling
								.authenticationEntryPoint(authenticationEntryPoint)
								.accessDeniedHandler(accessDeniedHandler));

		http.sessionManagement(
				sessionManagement ->
						sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}

	public TokenAuthenticationFilter generateAuthenticationFilter() {
		TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter();
		tokenAuthenticationFilter.setAuthenticationManager(new ProviderManager(tokenAuthProvider));
		return tokenAuthenticationFilter;
	}

	public OncePerRequestFilter getTokenInvalidExceptionHandlerFilter() {
		return new TokenInvalidExceptionHandlerFilter();
	}

	@Bean
	@Profile("!prod")
	public WebSecurityCustomizer localWebSecurityFilterIgnoreCustomizer() {
		return web ->
				web.ignoring()
						.requestMatchers(
								HttpMethod.GET,
								"/actuator/health",
								"/error",
								"/docs/swagger-ui/*",
								"/swagger-ui/*",
								"/swagger-resources/**",
								"/v3/api-docs/**",
								"/openapi3.yaml",
								"/reports/**",
								"/**")
						.requestMatchers(
								HttpMethod.POST, "/api/v1/members", "/api/v1/members/basic", "/api/callback/apple");
	}

	@Bean
	@Profile("prod")
	public WebSecurityCustomizer prodWebSecurityFilterIgnoreCustomizer() {
		return web ->
				web.ignoring()
						.requestMatchers(
								HttpMethod.GET,
								"/actuator/health",
								"/error",
								"/docs/swagger-ui/*",
								"/swagger-ui/*",
								"/swagger-resources/**",
								"/v3/api-docs/**",
								"/openapi3.yaml",
								"/reports/**",
								"/api/v1/**",
								"/swagger")
						.requestMatchers(HttpMethod.POST, "/api/v1/members");
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.addAllowedOriginPattern(corsProperties.getOriginPatterns());
		//		configuration.addAllowedOrigin("https://appleid.apple.com");
		configuration.addAllowedHeader(corsProperties.getAllowedHeaders());
		configuration.addAllowedMethod(corsProperties.getAllowedMethods());
		configuration.setAllowCredentials(corsProperties.getAllowCredentials());

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration(corsProperties.getPathPattern(), configuration);
		return source;
	}
}
