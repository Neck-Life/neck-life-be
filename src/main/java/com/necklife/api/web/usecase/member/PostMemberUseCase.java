package com.necklife.api.web.usecase.member;

import com.necklife.api.repository.member.dto.response.PostMemberRepoResponse;
import com.necklife.api.service.oauth.Oauth2UserService;
import com.necklife.api.service.oauth.Oauth2UserServiceV2;
import com.necklife.api.web.usecase.dto.request.member.PostMemberUseCaseRequest;
import com.necklife.api.web.usecase.dto.response.member.PostMemberUseCaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostMemberUseCase {

	private final Oauth2UserService oauth2UserService;
	private final Oauth2UserServiceV2 oauth2UserServiceV2;

	@Transactional(readOnly = false)
	public PostMemberUseCaseResponse execute(PostMemberUseCaseRequest request) {

		PostMemberRepoResponse savedMember = null;

		if (request.getTimeZone() == null || request.getLanguage() == null) {
			savedMember = oauth2UserService.findOrSaveMember(request.getCode(), request.getProvider());
		} else {
			savedMember =
					oauth2UserServiceV2.findOrSaveMember(
							request.getCode(),
							request.getProvider(),
							request.getTimeZone(),
							request.getLanguage());
		}

		return PostMemberUseCaseResponse.builder()
				.id(savedMember.getId())
				.email(savedMember.getEmail())
				.provider(savedMember.getProvider())
				.status(savedMember.getStatus())
				.build();
	}
}
