package com.necklife.api.security.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class DelegatedAccessDeniedHandler implements AccessDeniedHandler {

	private final HandlerExceptionResolver handlerExceptionResolver;

	@Override
	public void handle(
			HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException) {
		handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
	}
}
