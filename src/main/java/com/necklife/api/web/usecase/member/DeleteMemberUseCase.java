package com.necklife.api.web.usecase.member;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.web.client.member.AppleMemberClient;
import com.necklife.api.web.usecase.dto.response.member.DeleteMemberUseCaseResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeleteMemberUseCase {

	private final MemberRepository memberRepository;
	private final AppleMemberClient appleMemberClient;

	@Transactional
	public DeleteMemberUseCaseResponse execute(Long memberId) {
		Optional<MemberEntity> findMember = memberRepository.findById(memberId);
		if (findMember.isEmpty()) {
			throw new IllegalArgumentException("존재하지 않는 회원입니다.");
		}
		MemberEntity member = findMember.get();
		switch (member.getOauthProvider().toString()) {
				//            case "google":
				//                socialMemberData = googleOauthService.execute(id_token);
				//                break;
				//            case "kakao":
				//                socialMemberData = kaKaoOauthService.execute(id_token);
				//                break;
			case "APPLE":
				appleMemberClient.revokeToken(member.getOauthRefreshToken());
				break;
			case "NONE":
				break;
			default:
				throw new RuntimeException("제공하지 않는 인증기관입니다.");
		}

		member.delete();
		LocalDateTime deletedAt = member.getDeletedAt();

		return new DeleteMemberUseCaseResponse(member.getId(), deletedAt);
	}
}