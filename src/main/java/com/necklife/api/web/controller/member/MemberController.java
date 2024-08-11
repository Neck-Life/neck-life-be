package com.necklife.api.web.controller.member;

import com.necklife.api.security.authentication.authority.Roles;
import com.necklife.api.security.authentication.token.TokenUserDetailsService;
import com.necklife.api.security.token.AuthToken;
import com.necklife.api.security.token.TokenGenerator;
import com.necklife.api.security.token.TokenResolver;
import com.necklife.api.web.dto.request.member.*;
import com.necklife.api.web.dto.response.member.*;
import com.necklife.api.web.support.ApiResponse;
import com.necklife.api.web.support.ApiResponseGenerator;
import com.necklife.api.web.support.MessageCode;
import com.necklife.api.web.usecase.dto.response.member.*;
import com.necklife.api.web.usecase.member.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "Member API")
public class MemberController {

	private final TokenGenerator tokenGenerator;
	private final TokenResolver tokenResolver;

	private final PostMemberUseCase postMemberUseCase;
	private final DeleteMemberUseCase deleteMemberUseCase;
	private final GetMemberDetailUseCase getMemberDetailUseCase;
	private final GetMemberTokenDetailUseCase getMemberTokenDetailUseCase;
	private final PostBasicMemberUseCase postBasicMemberUseCase;

	private final PostPaymentUseCase postPaymentUseCase;
	private final PostRefoundPaymentUseCase postRefoundPaymentUseCase;

	private final TokenUserDetailsService tokenUserDetailsService;

	// todo usecase in/out 객체로 분리

	/* 로그인과 회원가입은 동시에 이루어집니다. */
	@PostMapping()
	public ApiResponse<ApiResponse.SuccessBody<PostMemberResponse>> postMember(
			@Valid @RequestBody PostOauthMemberBody postOauthMemberBody) {
		PostMemberUseCaseResponse useCaseResponse =
				postMemberUseCase.execute(postOauthMemberBody.getCode(), postOauthMemberBody.getProvider());
		AuthToken authToken =
				tokenGenerator.generateAuthToken(useCaseResponse.getId(), List.of(Roles.ROLE_USER));
		PostMemberResponse response =
				PostMemberResponse.builder()
						.id(useCaseResponse.getId())
						.email(useCaseResponse.getEmail())
						.provider(useCaseResponse.getProvider())
						.status(useCaseResponse.getStatus())
						.accessToken(authToken.getAccessToken())
						.refreshToken(authToken.getRefreshToken())
						.build();
		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.RESOURCE_CREATED);
	}

	@PostMapping("/basic")
	public ApiResponse<ApiResponse.SuccessBody<PostMemberResponse>> postBasicMember(
			@Valid @RequestBody PostBasicMemberBody postBasicMemberBody) {
		PostMemberUseCaseResponse useCaseResponse =
				postBasicMemberUseCase.execute(
						postBasicMemberBody.getEmail(), postBasicMemberBody.getPassword());
		AuthToken authToken =
				tokenGenerator.generateAuthToken(useCaseResponse.getId(), List.of(Roles.ROLE_USER));
		PostMemberResponse response =
				PostMemberResponse.builder()
						.id(useCaseResponse.getId())
						.email(useCaseResponse.getEmail())
						.provider(useCaseResponse.getProvider())
						.status(useCaseResponse.getStatus())
						.accessToken(authToken.getAccessToken())
						.refreshToken(authToken.getRefreshToken())
						.build();

		return ApiResponseGenerator.success(response, HttpStatus.CREATED, MessageCode.RESOURCE_CREATED);
	}

	// todo @AuthenticationPrincipal TokenUserDetails userDetails로 교체
	@DeleteMapping()
	public ApiResponse<ApiResponse.SuccessBody<DeleteMemberResponse>> deleteMember(
			HttpServletRequest httpServletRequest) {
		String memberId = findMemberByToken(httpServletRequest);
		//		Long memberId = 1L;
		DeleteMemberUseCaseResponse useCaseResponse = deleteMemberUseCase.execute(memberId);
		DeleteMemberResponse response =
				DeleteMemberResponse.builder()
						.id(useCaseResponse.getId())
						.deletedAt(useCaseResponse.getDeletedAt())
						.build();
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.RESOURCE_DELETED);
	}

	@GetMapping()
	public ApiResponse<ApiResponse.SuccessBody<GetMemberResponse>> getMember(
			HttpServletRequest httpServletRequest) {
		String memberId = findMemberByToken(httpServletRequest);
		//				Long memberId = Long.valueOf(userDetails.getUsername());
		//		Long memberId = 1L;
		GetMemberDetailUseCaseResponse useCaseResponse = getMemberDetailUseCase.execute(memberId);
		GetMemberResponse response =
				GetMemberResponse.builder()
						.id(useCaseResponse.getId())
						.email(useCaseResponse.getEmail())
						.provider(useCaseResponse.getProvider())
						.status(useCaseResponse.getStatus())
						.build();
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PostMapping("/token")
	public ApiResponse<ApiResponse.SuccessBody<MemberTokenResponse>> refreshMemberAuthToken(
			@Valid @RequestBody RefreshMemberAuthTokenBody memberAuthTokenBody) {
		String memberId =
				tokenResolver
						.resolveId(memberAuthTokenBody.getRefreshToken())
						.orElseThrow(() -> new IllegalArgumentException("Invalid token"));
		GetMemberTokenDetailUseCaseResponse useCaseResponse =
				getMemberTokenDetailUseCase.execute(memberId);
		AuthToken authToken =
				tokenGenerator.generateAuthToken(useCaseResponse.getId(), List.of(Roles.ROLE_USER));
		MemberTokenResponse response =
				MemberTokenResponse.builder()
						.accessToken(authToken.getAccessToken())
						.refreshToken(authToken.getRefreshToken())
						.build();
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PostMapping("/payment")
	public ApiResponse<ApiResponse.SuccessBody<PostPaymentUseCaseResponse>> postPayment(
			HttpServletRequest httpServletRequest, PostPaymentRequest postPaymentRequest) {

		String memberId = findMemberByToken(httpServletRequest);
		//				Long memberId = Long.valueOf(userDetails.getUsername());
		//		Long memberId = 1L;
		PostPaymentUseCaseResponse response =
				postPaymentUseCase.execute(
						memberId,
						postPaymentRequest.getDate(),
						postPaymentRequest.getMonths(),
						postPaymentRequest.getWon());

		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PostMapping("/refound")
	public ApiResponse<ApiResponse.SuccessBody<PostRefoundPaymentUseCaseResponse>> refoundPayment(
			HttpServletRequest httpServletRequest, PostRefoundPaymentRequest postRefoundPaymentRequest) {

		String memberId = findMemberByToken(httpServletRequest);
		//				Long memberId = Long.valueOf(userDetails.getUsername());
		//		Long memberId = 1L;
		PostRefoundPaymentUseCaseResponse response =
				postRefoundPaymentUseCase.execute(
						memberId,
						postRefoundPaymentRequest.getDate(),
						postRefoundPaymentRequest.getReason(),
						postRefoundPaymentRequest.getRefoundWon());

		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping("/streak")
	public ApiResponse<ApiResponse.SuccessBody<Void>> getMemberStreak(
			HttpServletRequest httpServletRequest) {
		String memberId = findMemberByToken(httpServletRequest);
		//		Long memberId = 1L;
		return ApiResponseGenerator.success(null, HttpStatus.OK, MessageCode.RESOURCE_DELETED);
	}

	private String findMemberByToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		String substring = authorization.substring(7, authorization.length());
		UserDetails userDetails = tokenUserDetailsService.loadUserByUsername(substring);
		return userDetails.getUsername();
	}
}
