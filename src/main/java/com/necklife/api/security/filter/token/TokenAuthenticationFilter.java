package com.necklife.api.security.filter.token;

import com.walking.api.security.exception.AccessTokenInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
public class TokenAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

	@Override
	protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
		return resolveAccessToken(request);
	}

	@Override
	protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
		return resolveAccessToken(request);
	}

	private String resolveAccessToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		if (Objects.isNull(authorization)) {
			AccessTokenInvalidException exception =
					getAccessTokenInvalidException("Authorization header is missing");
			throw exception;
		}
		return AccessTokenResolver.resolve(authorization);
	}

	private AccessTokenInvalidException getAccessTokenInvalidException(String message) {
		AccessTokenInvalidException exception = new AccessTokenInvalidException(message);
		log.warn(exception.getMessage());
		return exception;
	}
}
