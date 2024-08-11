package com.necklife.api.web.controller.goal;

import com.necklife.api.entity.goal.GoalEntity;
import com.necklife.api.entity.goal.GoalType;
import com.necklife.api.security.authentication.token.TokenUserDetailsService;
import com.necklife.api.web.dto.request.goal.DeleteGoalRequest;
import com.necklife.api.web.dto.request.goal.GoalRequest;
import com.necklife.api.web.support.ApiResponse;
import com.necklife.api.web.support.ApiResponseGenerator;
import com.necklife.api.web.support.MessageCode;
import com.necklife.api.web.usecase.dto.response.goal.GoalHistoryResponse;
import com.necklife.api.web.usecase.dto.response.goal.GoalResponse;
import com.necklife.api.web.usecase.dto.response.streak.StreakResponse;
import com.necklife.api.web.usecase.goal.*;
import com.necklife.api.web.usecase.steak.GetMemberStreakUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/goals")
@RequiredArgsConstructor
@Tag(name = "Goal")
public class GoalController {

	private final TokenUserDetailsService tokenUserDetailsService;

	private final AddGoalsUseCase addGoalsUseCase;
	private final DeleteGoalsUseCase deleteGoalsUseCase;
	private final GetDefaultGoalsUsecase getDefaultGoalsUsecase;
	private final GetGoalsHistoryUseCase getGoalsHistoryUsecase;
	private final GetMemberStreakUseCase getMemberStreakUseCase;
	private final GetGoalsUseCase getGoalsUsecase;
	private final UpdateGoalsUseCase updateGoalsUseCase;

	@GetMapping("/default")
	public ApiResponse<ApiResponse.SuccessBody<GoalResponse>> getDefaultGoals(
			HttpServletRequest httpServletRequest) {
		String memberId = findMemberByToken(httpServletRequest);
		//		Long memberId = 1L;

		GoalResponse response = getDefaultGoalsUsecase.execute();

		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping()
	public ApiResponse<ApiResponse.SuccessBody<GoalResponse>> getGoals(
			HttpServletRequest httpServletRequest) {
		String memberId = findMemberByToken(httpServletRequest);
		//		Long memberId = 1L;

		GoalResponse response = getGoalsUsecase.execute(memberId);

		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PostMapping()
	public ApiResponse<ApiResponse.SuccessBody<GoalResponse>> addGoals(
			HttpServletRequest httpServletRequest, @Valid @RequestBody GoalRequest goalRequest) {
		String memberId = findMemberByToken(httpServletRequest);
		//		Long memberId = 1L;
		List<GoalEntity.GoalDetail> newGoalDetails =
				goalRequest.getGoals().stream()
						.map(
								goal ->
										GoalEntity.GoalDetail.builder()
												.orders(goal.getOrder())
												.type(GoalType.valueOf(goal.getType().toUpperCase()))
												.description(goal.getDescription())
												.targetValue(goal.getTarget_value())
												.build())
						.toList();

		GoalResponse response = addGoalsUseCase.execute(memberId, newGoalDetails);

		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PutMapping()
	public ApiResponse<ApiResponse.SuccessBody<GoalResponse>> updateGoals(
			HttpServletRequest httpServletRequest, @Valid @RequestBody GoalRequest goalRequest) {
		String memberId = findMemberByToken(httpServletRequest);
		//		Long memberId = 1L;

		GoalResponse response =
				updateGoalsUseCase.execute(
						memberId,
						goalRequest.getGoals().stream()
								.map(
										goal ->
												GoalEntity.GoalDetail.builder()
														.orders(goal.getOrder())
														.type(GoalType.valueOf(goal.getType().toUpperCase()))
														.description(goal.getDescription())
														.targetValue(goal.getTarget_value())
														.build())
								.toList());

		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.RESOURCE_MODIFIED);
	}

	@DeleteMapping()
	public ApiResponse<ApiResponse.SuccessBody<GoalResponse>> deleteGoals(
			HttpServletRequest httpServletRequest,
			@Valid @RequestBody DeleteGoalRequest deleteGoalRequest) {
		String memberId = findMemberByToken(httpServletRequest);

		GoalResponse response = deleteGoalsUseCase.execute(memberId, deleteGoalRequest.getGoalIds());
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.RESOURCE_DELETED);
	}


	@GetMapping("/history")
	public ApiResponse<ApiResponse.SuccessBody<GoalHistoryResponse>> getGoalHistory(
			HttpServletRequest httpServletRequest) {
		String memberId = findMemberByToken(httpServletRequest);
		//		Long memberId = 1L;

		GoalHistoryResponse response = getGoalsHistoryUsecase.execute(memberId);
		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}


	@GetMapping("/streak")
	public ApiResponse<ApiResponse.SuccessBody<StreakResponse>> getGoalsStreak(
			HttpServletRequest httpServletRequest) {
		String memberId = findMemberByToken(httpServletRequest);
		//		Long memberId = 1L;

		StreakResponse response = getMemberStreakUseCase.execute(memberId);

		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	private String findMemberByToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		String substring = authorization.substring(7, authorization.length());
		UserDetails userDetails = tokenUserDetailsService.loadUserByUsername(substring);
		return userDetails.getUsername();
	}
}
