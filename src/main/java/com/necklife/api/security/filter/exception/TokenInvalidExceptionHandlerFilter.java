package com.necklife.api.security.filter.exception;

import com.necklife.api.security.exception.AccessTokenInvalidException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TokenInvalidExceptionHandlerFilter extends OncePerRequestFilter {

	private static final String CONTENT_TYPE = "application/json; charset=UTF-8";

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (AccessTokenInvalidException e) {
			setError(response, e);
		}
	}

	private void setError(HttpServletResponse response, Exception e) throws IOException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType(CONTENT_TYPE);
		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now());
		response.getWriter().write(errorResponse.toString());
	}

	@RequiredArgsConstructor
	private static class ErrorResponse {
		private static String code = "fail.authentication";
		private static String message = "인증이 필요해요.";
		private final LocalDateTime timestamp;

		@Override
		public String toString() {
			return "{"
					+ "\"code\" : \""
					+ code
					+ "\""
					+ ", \"message\" : \""
					+ message
					+ "\""
					+ ", \"timestamp\" : \""
					+ timestamp
					+ "\"}";
		}
	}
}
