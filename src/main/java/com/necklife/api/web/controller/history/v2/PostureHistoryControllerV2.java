package com.necklife.api.web.controller.history.v2;

import com.necklife.api.entity.history.PoseStatus;
import com.necklife.api.security.authentication.token.TokenUserDetails;
import com.necklife.api.web.dto.request.history.PostPostureHistoryBody;
import com.necklife.api.web.dto.request.history.PostRawHistoryBody;
import com.necklife.api.web.support.ApiResponse;
import com.necklife.api.web.support.ApiResponseGenerator;
import com.necklife.api.web.usecase.dto.request.history.PostHistoryRequest;
import com.necklife.api.web.usecase.history.PostRawDataUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/api/v2/history")
@RequiredArgsConstructor
public class PostureHistoryControllerV2 {

    private final PostRawDataUseCase postRawDataUseCase;

    @PostMapping
    public ApiResponse<ApiResponse.Success> postHistory(
            @AuthenticationPrincipal TokenUserDetails tokenUserDetails,
            @Valid @RequestBody PostRawHistoryBody postureHistoryBody) {

        String memberId = tokenUserDetails.getId();

        postRawDataUseCase.execute(memberId, postureHistoryBody.getHistorys(), postureHistoryBody.getRawData());


        return ApiResponseGenerator.success(HttpStatus.OK);
    }

    @GetMapping("/raw")
    public ApiResponse<ApiResponse.Success> getRawPosition(
            @AuthenticationPrincipal TokenUserDetails tokenUserDetails) {

        String memberId = tokenUserDetails.getId();


        return ApiResponseGenerator.success(HttpStatus.OK);
    }







}
