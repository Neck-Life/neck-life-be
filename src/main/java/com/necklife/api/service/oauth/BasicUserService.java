package com.necklife.api.service.oauth;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.member.OauthProvider;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.repository.member.dto.response.PostMemberRepoResponse;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicUserService {

	private final MemberRepository memberRepository;

	public PostMemberRepoResponse execute(String email, String password) {
		AtomicBoolean isNew = new AtomicBoolean(false);
		MemberEntity member =
				memberRepository
						.findByEmailAndOauthProviderAndDeletedAtIsNull(email, OauthProvider.valueOf("NONE"))
						.orElseGet(
								() -> {
									MemberEntity newMember =
											MemberEntity.builder()
													.email(email)
													.password(password)
													.isSocial(false)
													.oauthProvider(OauthProvider.valueOf("NONE"))
													.build();

									isNew.set(true);
									newMember.unpaid();

									return memberRepository.save(newMember);
								});

		return PostMemberRepoResponse.builder()
				.id(member.getId())
				.email(member.getEmail())
				.provider(member.getOauthProvider().toString())
				.status(member.getStatus().name())
				.isNew(isNew.get())
				.build();
	}
}
