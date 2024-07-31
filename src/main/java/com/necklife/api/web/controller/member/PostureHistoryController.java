package com.necklife.api.web.controller.member;

import com.necklife.api.security.authentication.token.TokenUserDetails;
import com.necklife.api.web.dto.request.history.PostPostureHistoryBody;
import com.necklife.api.web.support.ApiResponse;
import com.necklife.api.web.support.ApiResponseGenerator;
import com.necklife.api.web.support.MessageCode;
import com.necklife.api.web.usecase.dto.response.history.GetHistoryPointResponse;
import com.necklife.api.web.usecase.dto.response.history.GetMonthDetailResponse;
import com.necklife.api.web.usecase.dto.response.history.GetYearDetailResponse;
import com.necklife.api.web.usecase.history.GetHistoryPointUseCase;
import com.necklife.api.web.usecase.history.GetMonthDetailUseCase;
import com.necklife.api.web.usecase.history.GetYearDetailUseCase;
import com.necklife.api.web.usecase.history.PostHistoryUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class PostureHistoryController {

	private final PostHistoryUseCase postHistoryUseCase;
	private final GetYearDetailUseCase getYearDetailUseCase;
	private final GetMonthDetailUseCase getMonthDetailUseCase;
	private final GetHistoryPointUseCase getHistoryPointUseCase;

	@PostMapping
	public ApiResponse<ApiResponse.Success> postHistory(
			@Valid @RequestBody PostPostureHistoryBody postureHistoryBody) {

		postHistoryUseCase.execute();

		return ApiResponseGenerator.success(HttpStatus.OK);
	}

	@GetMapping("/month")
	public ApiResponse<ApiResponse.SuccessBody<GetMonthDetailResponse>> getMonthHistory(
			@AuthenticationPrincipal TokenUserDetails userDetails) {
		//    Long memberId = Long.valueOf(userDetails.getUsername());

		GetMonthDetailResponse monthDetailResponse = getMonthDetailUseCase.execute();

		return ApiResponseGenerator.success(monthDetailResponse, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping("/year")
	public ApiResponse<ApiResponse.SuccessBody<GetYearDetailResponse>> getYearHistory(
			@AuthenticationPrincipal TokenUserDetails userDetails) {
		//        Long memberId = Long.valueOf(userDetails.getUsername());

		GetYearDetailResponse yearDetailResponse = getYearDetailUseCase.execute();
		return ApiResponseGenerator.success(yearDetailResponse, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping("/point")
	public ApiResponse<ApiResponse.SuccessBody<GetHistoryPointResponse>> getHistoryPoint(
			@AuthenticationPrincipal TokenUserDetails userDetails) {
		//    Long memberId = Long.valueOf(userDetails.getUsername());

		GetHistoryPointResponse historyPointResponse = getHistoryPointUseCase.execute();

		return ApiResponseGenerator.success(historyPointResponse, HttpStatus.OK, MessageCode.SUCCESS);
	}
}
