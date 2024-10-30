package com.necklife.api.web.usecase.member;

import com.necklife.api.repository.member.dto.response.PostMemberRepoResponse;
import com.necklife.api.service.oauth.BasicUserService;
import com.necklife.api.service.oauth.BasicUserServiceV2;
import com.necklife.api.web.usecase.dto.request.member.PostBasicMemberUseCaseRequest;
import com.necklife.api.web.usecase.dto.response.member.PostMemberUseCaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostBasicMemberUseCase {

	private final BasicUserService basicUserService;
	private final BasicUserServiceV2 basicUserServiceV2;

	@Transactional(readOnly = false)
	public PostMemberUseCaseResponse execute(PostBasicMemberUseCaseRequest request) {

		PostMemberRepoResponse savedMember = null;
		if (request.getTimeZone() == null || request.getLanguage() == null) {

			savedMember = basicUserService.execute(request.getEmail(), request.getPassword());

		} else {
			savedMember =
					basicUserServiceV2.execute(
							request.getEmail(),
							request.getPassword(),
							request.getTimeZone(),
							request.getLanguage(),
							request.getNotificationToken());
		}

		return PostMemberUseCaseResponse.builder()
				.id(savedMember.getId())
				.email(savedMember.getEmail())
				.provider(savedMember.getProvider())
				.status(savedMember.getStatus())
				.build();
	}
}
