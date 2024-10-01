package com.necklife.api.web.controller.history;

import com.necklife.api.entity.history.PoseStatus;
import com.necklife.api.security.authentication.token.TokenUserDetails;
import com.necklife.api.security.authentication.token.TokenUserDetailsService;
import com.necklife.api.security.token.TokenGenerator;
import com.necklife.api.security.token.TokenResolver;
import com.necklife.api.web.dto.request.history.HistoryPointEnum;
import com.necklife.api.web.dto.request.history.PostPostureHistoryBody;
import com.necklife.api.web.dto.request.history.PostRawHistoryBody;
import com.necklife.api.web.support.ApiResponse;
import com.necklife.api.web.support.ApiResponseGenerator;
import com.necklife.api.web.support.MessageCode;
import com.necklife.api.web.usecase.dto.request.history.GetMonthlyHistoryRequest;
import com.necklife.api.web.usecase.dto.request.history.GetYearHistoryRequest;
import com.necklife.api.web.usecase.dto.request.history.PostHistoryRequest;
import com.necklife.api.web.usecase.dto.response.history.GetHistoryPointResponse;
import com.necklife.api.web.usecase.dto.response.history.GetMonthlyDetailResponse;
import com.necklife.api.web.usecase.dto.response.history.GetYearDetailResponse;
import com.necklife.api.web.usecase.history.GetHistoryPointUseCase;
import com.necklife.api.web.usecase.history.GetMonthlyDetailUseCase;
import com.necklife.api.web.usecase.history.GetYearDetailUseCase;
import com.necklife.api.web.usecase.history.PostHistoryUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class PostureHistoryController {

	private final TokenUserDetailsService tokenUserDetailsService;

	private final TokenGenerator tokenGenerator;
	private final TokenResolver tokenResolver;

	private final PostHistoryUseCase postHistoryUseCase;
	private final GetYearDetailUseCase getYearDetailUseCase;
	private final GetMonthlyDetailUseCase getMonthlyDetailUseCase;
	private final GetHistoryPointUseCase getHistoryPointUseCase;

	@PostMapping
	public ApiResponse<ApiResponse.Success> postHistory(
			HttpServletRequest httpServletRequest,
			@Valid @RequestBody PostPostureHistoryBody postureHistoryBody) {
		String memberId = findMemberByToken(httpServletRequest);

		List<TreeMap<LocalDateTime, PoseStatus>> convertedMapList = new ArrayList<>();

		for (Map<LocalDateTime, String> subHistory : postureHistoryBody.getHistorys()) {

			TreeMap<LocalDateTime, PoseStatus> convertedMap = new TreeMap<>();
			for (Map.Entry<LocalDateTime, String> entry : subHistory.entrySet()) {
				LocalDateTime key = entry.getKey();

				String value = entry.getValue();
				PoseStatus poseStatus;

				try {
					poseStatus = PoseStatus.valueOf(value.toUpperCase());
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException("잘못된 PoseStatus입니다.");
				}

				convertedMap.put(key, poseStatus);
			}
			convertedMapList.add(convertedMap);
		}

		PostHistoryRequest postHistoryRequest = new PostHistoryRequest(memberId, convertedMapList);

		postHistoryUseCase.execute(postHistoryRequest);

		return ApiResponseGenerator.success(HttpStatus.OK);
	}

	@GetMapping("/monthly")
	public ApiResponse<ApiResponse.SuccessBody<GetMonthlyDetailResponse>> getMonthHistory(
			HttpServletRequest httpServletRequest,
			@RequestParam("year") Integer year,
			@RequestParam("month") Integer month) {
		String memberId = findMemberByToken(httpServletRequest);
		checkYearAndMonth(year, month);
		GetMonthlyDetailResponse monthDetailResponse =
				getMonthlyDetailUseCase.execute(new GetMonthlyHistoryRequest(memberId, year, month));

		return ApiResponseGenerator.success(monthDetailResponse, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping("/year")
	public ApiResponse<ApiResponse.SuccessBody<GetYearDetailResponse>> getYearHistory(
			HttpServletRequest httpServletRequest, @RequestParam("year") Integer year) {
		String memberId = findMemberByToken(httpServletRequest);
		checkYear(year);

		GetYearDetailResponse yearDetailResponse =
				getYearDetailUseCase.execute(new GetYearHistoryRequest(memberId, year));
		return ApiResponseGenerator.success(yearDetailResponse, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@GetMapping("/point")
	public ApiResponse<ApiResponse.SuccessBody<GetHistoryPointResponse>> getPoint(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Valid @RequestParam("type") HistoryPointEnum historyPointRequest ) {
		String username = userDetails.getUsername();

		GetHistoryPointResponse getHistoryPointResponse = getHistoryPointUseCase.execute(username,historyPointRequest);

		return ApiResponseGenerator.success(
				getHistoryPointResponse, HttpStatus.OK, MessageCode.SUCCESS);
	}

	@PostMapping("/raw")
	public ApiResponse<ApiResponse.Success> postRawData(
			@AuthenticationPrincipal TokenUserDetails userDetails,
			@Valid @RequestBody List<PostRawHistoryBody> postRawHistoryBodyList) {
		String username = userDetails.getUsername();




		return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.SUCCESS);
	}







	private static void checkYearAndMonth(Integer year, Integer month) {

		if (year < 2023 || month < 1 || month > 12) {
			throw new IllegalArgumentException("잘못된 Request Parameter입니다.");
		}
	}

	private static void checkYear(Integer year) {
		if (year < 2023) {
			throw new IllegalArgumentException("잘못된 Request Parameter입니다.");
		}
	}

	private String findMemberByToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		String substring = authorization.substring(7, authorization.length());
		UserDetails userDetails = tokenUserDetailsService.loadUserByUsername(substring);
		return userDetails.getUsername();
	}
}
