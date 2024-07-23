package com.necklife.api.web.controller.member;

import com.necklife.api.web.support.ApiResponse;
import com.necklife.api.web.support.ApiResponseGenerator;
import com.necklife.api.web.support.MessageCode;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
public class AppleOauthController {

	@PostMapping("/api/callback/apple")
	public ApiResponse<ApiResponse.SuccessBody<String>> postBasicMember(
			@Valid @RequestBody String postData) {

		log.info("postData: {}", postData);
		return ApiResponseGenerator.success(postData, HttpStatus.OK, MessageCode.RESOURCE_CREATED);
	}
}
