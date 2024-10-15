package com.necklife.api.web.usecase.member;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.member.WithDrawReason;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.repository.member.WithDrawReasonRepository;
import com.necklife.api.web.client.member.AppleMemberClient;
import com.necklife.api.web.usecase.dto.response.member.DeleteMemberUseCaseResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeleteMemberUseCase {

	private final MemberRepository memberRepository;
	private final AppleMemberClient appleMemberClient;
	private final WithDrawReasonRepository withDrawReasonRepository;

	@Transactional
	public DeleteMemberUseCaseResponse execute(String memberId, String withDrawReason) {
		Optional<MemberEntity> findMember = memberRepository.findById(memberId);
		withDrawReason = isEmptyString(withDrawReason);

		if (findMember.isEmpty()) {
			throw new IllegalArgumentException("존재하지 않는 회원입니다.");
		}

		withDrawReasonRepository.save(
				WithDrawReason.builder().member(findMember.get()).withDrawReason(withDrawReason).build());

		MemberEntity member = findMember.get();
		switch (member.getOauthProvider().toString()) {
			case "GOOGLE":
				//				socialMemberData = googleOauthService.execute(id_token);
				break;
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

		MemberEntity deletedMember = member.delete();
		LocalDateTime deletedAt = deletedMember.getDeletedAt();
		memberRepository.save(deletedMember);

		return new DeleteMemberUseCaseResponse(deletedMember.getId(), deletedAt);
	}

	private String isEmptyString(String string) {

		boolean hasText = StringUtils.hasText(string);

		if (hasText) {
			return string;
		} else {
			return "";
		}
	}
}
