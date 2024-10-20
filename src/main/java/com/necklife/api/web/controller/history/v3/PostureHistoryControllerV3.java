package com.necklife.api.web.controller.history.v3;

import com.necklife.api.security.authentication.token.TokenUserDetails;
import com.necklife.api.web.dto.request.history.PostRawPitchTiltHistoryBody;
import com.necklife.api.web.support.ApiResponse;
import com.necklife.api.web.support.ApiResponseGenerator;
import com.necklife.api.web.support.MessageCode;
import com.necklife.api.web.usecase.dto.request.history.GetMonthlyHistoryRequest;
import com.necklife.api.web.usecase.dto.response.history.GetRawHistoryResponse;
import com.necklife.api.web.usecase.dto.response.history.v3.GetMonthlyDetailPitchForwardTiltResponse;
import com.necklife.api.web.usecase.history.GetRawDataPositionUseCase;
import com.necklife.api.web.usecase.history.v3.GetPitchTiltForwardUseCase;
import com.necklife.api.web.usecase.history.v3.PostRawPitchTiltForwardUseCase;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v3/history")
@RequiredArgsConstructor
public class PostureHistoryControllerV3 {

	private final PostRawPitchTiltForwardUseCase postRawPitchTiltForwardUseCase;
	private final GetRawDataPositionUseCase getRawDataPositionUseCase;
	private final GetPitchTiltForwardUseCase getPitchTiltForwardUseCase;

	@PostMapping
	public ApiResponse<ApiResponse.Success> postHistory(
			@AuthenticationPrincipal TokenUserDetails tokenUserDetails,
			@Valid @RequestBody PostRawPitchTiltHistoryBody postureHistoryBody) {

		String memberId = tokenUserDetails.getId();

		postRawPitchTiltForwardUseCase.execute(
				memberId,
				postureHistoryBody.getPitch(),
				postureHistoryBody.getForward(),
				postureHistoryBody.getTilt(),
				postureHistoryBody.getRawData());

		return ApiResponseGenerator.success(HttpStatus.OK);
	}

	@GetMapping("/raw")
	public ApiResponse<ApiResponse.SuccessBody<GetRawHistoryResponse>> getRawData(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@RequestParam("timestamp") LocalDateTime timestamp) {
		String username = userDetails.getUsername();

		GetRawHistoryResponse response = getRawDataPositionUseCase.execute(username, timestamp);

		return ApiResponseGenerator.success(response, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping("/monthly")
	public ApiResponse<ApiResponse.SuccessBody<GetMonthlyDetailPitchForwardTiltResponse>>
			getMonthHistory(
					@AuthenticationPrincipal TokenUserDetails userDetails,
					@RequestParam("year") Integer year,
					@RequestParam("month") Integer month) {
		String memberId = userDetails.getUsername();
		checkYearAndMonth(year, month);
		GetMonthlyDetailPitchForwardTiltResponse monthDetailResponse =
				getPitchTiltForwardUseCase.execute(new GetMonthlyHistoryRequest(memberId, year, month));

		return ApiResponseGenerator.success(monthDetailResponse, HttpStatus.OK, MessageCode.SUCCESS);
	}

	private static void checkYearAndMonth(Integer year, Integer month) {

		if (year < 2023 || month < 1 || month > 12) {
			throw new IllegalArgumentException("잘못된 Request Parameter입니다.");
		}
	}
}
