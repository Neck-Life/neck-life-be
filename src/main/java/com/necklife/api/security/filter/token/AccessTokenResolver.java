package com.necklife.api.security.filter.token;

import com.walking.api.security.exception.AccessTokenInvalidException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public class AccessTokenResolver {
	private static final Pattern PATTERN_AUTHORIZATION_HEADER = Pattern.compile("^[Bb]earer (.*)$");

	public static String resolve(@NotNull String authorization) {
		var matcher = PATTERN_AUTHORIZATION_HEADER.matcher(authorization);
		if (!matcher.matches()) {
			AccessTokenInvalidException exception =
					getAccessTokenInvalidException("Authorization header is not a Bearer token");
			throw exception;
		}
		return matcher.group(1);
	}

	private AccessTokenInvalidException getAccessTokenInvalidException(String message) {
		AccessTokenInvalidException exception = new AccessTokenInvalidException(message);
		log.warn(exception.getMessage());
		return exception;
	}
}
