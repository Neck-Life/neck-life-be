package com.necklife.api.web.controller.member;

import com.necklife.api.notification.service.FCMService;
import com.necklife.api.notification.service.PostureNotificationService;
import com.necklife.api.web.support.ApiResponse;
import com.necklife.api.web.support.ApiResponseGenerator;
import com.necklife.api.web.support.MessageCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class AppleOauthController {

	@Autowired private FCMService fcmService;

	@Autowired private PostureNotificationService postureNotificationService;

	@CrossOrigin(origins = {"https://appleid.apple.com"})
	@PostMapping("/api/callback/apple")
	public ApiResponse<ApiResponse.SuccessBody<String>> postBasicMember(HttpServletRequest request) {
		String postData = request.getParameter("code");

		log.debug("postData: {}", postData);
		return ApiResponseGenerator.success(postData, HttpStatus.OK, MessageCode.RESOURCE_CREATED);
	}

	@GetMapping("/api/callback")
	public ApiResponse<ApiResponse.SuccessBody<String>> test() {
		postureNotificationService.sendKoreaTimezoneKoreanNotifications();

		return ApiResponseGenerator.success("성공", HttpStatus.OK, MessageCode.RESOURCE_CREATED);
	}
}
