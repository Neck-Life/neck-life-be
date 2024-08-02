package com.necklife.api.web.controller.member;

import com.necklife.api.entity.history.PoseStatus;
import com.necklife.api.security.authentication.token.TokenUserDetails;
import com.necklife.api.web.dto.request.history.PostPostureHistoryBody;
import com.necklife.api.web.support.ApiResponse;
import com.necklife.api.web.support.ApiResponseGenerator;
import com.necklife.api.web.support.MessageCode;
import com.necklife.api.web.usecase.dto.request.history.GetMonthlyHistoryRequest;
import com.necklife.api.web.usecase.dto.request.history.GetYearHistoryRequest;
import com.necklife.api.web.usecase.dto.request.history.PostHistoryRequest;
import com.necklife.api.web.usecase.dto.request.history.PostSubHistoryDto;
import com.necklife.api.web.usecase.dto.response.history.GetMonthlyDetailResponse;
import com.necklife.api.web.usecase.dto.response.history.GetYearDetailResponse;
import com.necklife.api.web.usecase.history.GetHistoryPointUseCase;
import com.necklife.api.web.usecase.history.GetMonthlyDetailUseCase;
import com.necklife.api.web.usecase.history.GetYearDetailUseCase;
import com.necklife.api.web.usecase.history.PostHistoryUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class PostureHistoryController {

	private final PostHistoryUseCase postHistoryUseCase;
	private final GetYearDetailUseCase getYearDetailUseCase;
	private final GetMonthlyDetailUseCase getMonthlyDetailUseCase;

	@PostMapping
	public ApiResponse<ApiResponse.Success> postHistory(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Valid @RequestBody PostPostureHistoryBody postureHistoryBody) {
	    String memberId = userDetails.getUsername();

		List<PostSubHistoryDto> subHistories = postureHistoryBody.getHistory().stream()
				.map(subHistory -> new PostSubHistoryDto(subHistory.getStartAt(), PoseStatus.valueOf(subHistory.getStatus())))
				.collect(Collectors.toList());

		PostHistoryRequest postHistoryRequest = new PostHistoryRequest(memberId, postureHistoryBody.getStartAt(),
				postureHistoryBody.getEndAt(), subHistories);

		postHistoryUseCase.execute(postHistoryRequest);

		return ApiResponseGenerator.success(HttpStatus.OK);
	}

	@GetMapping("/monthly")
	public ApiResponse<ApiResponse.SuccessBody<GetMonthlyDetailResponse>> getMonthHistory(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@RequestParam("year") Integer year,
			@RequestParam("month") Integer month) {
		String memberId = userDetails.getUsername();
		checkYearAndMonth(year, month);
        GetMonthlyDetailResponse monthDetailResponse =
				getMonthlyDetailUseCase.execute(new GetMonthlyHistoryRequest(memberId, year, month));

		return ApiResponseGenerator.success(monthDetailResponse, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping("/year")
	public ApiResponse<ApiResponse.SuccessBody<GetYearDetailResponse>> getYearHistory(
			@AuthenticationPrincipal TokenUserDetails userDetails, @RequestParam("year") Integer year) {
		String memberId = userDetails.getUsername();
		checkYear(year);

		GetYearDetailResponse yearDetailResponse = getYearDetailUseCase.execute(new GetYearHistoryRequest(memberId, year));
		return ApiResponseGenerator.success(yearDetailResponse, HttpStatus.OK, MessageCode.SUCCESS);
	}

	private static void checkYearAndMonth(Integer year, Integer month) {
		if (year <2023 || month <1 || month >12) {
			throw new IllegalArgumentException("잘못된 Request Parameter입니다.");
		}
	}

	private static void checkYear(Integer year) {
		if (year <2023 ) {
			throw new IllegalArgumentException("잘못된 Request Parameter입니다.");
		}
	}


}
