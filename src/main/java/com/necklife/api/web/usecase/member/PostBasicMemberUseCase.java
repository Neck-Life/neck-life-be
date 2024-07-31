package com.necklife.api.web.usecase.member;

import com.necklife.api.repository.member.dto.response.PostMemberRepoResponse;
import com.necklife.api.service.oauth.BasicUserService;
import com.necklife.api.web.usecase.dto.response.member.PostMemberUseCaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostBasicMemberUseCase {

	private final BasicUserService basicUserService;

	@Transactional(readOnly = false)
	public PostMemberUseCaseResponse execute(String email, String password) {
		PostMemberRepoResponse savedMember = basicUserService.execute(email, password);

		return PostMemberUseCaseResponse.builder()
				.id(savedMember.getId())
				.email(savedMember.getEmail())
				.provider(savedMember.getProvider())
				.status(savedMember.getStatus())
				.build();
	}
}
