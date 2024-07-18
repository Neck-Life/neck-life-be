package com.necklife.api.web.usecase;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.web.usecase.dto.response.DeleteMemberUseCaseResponse;
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

	@Transactional
	public DeleteMemberUseCaseResponse execute(Long memberId) {
		Optional<MemberEntity> findMember = memberRepository.findById(memberId);
		if (findMember.isEmpty()) {
			throw new IllegalArgumentException("존재하지 않는 회원입니다.");
		}
		MemberEntity member = findMember.get();
		member.delete();
		LocalDateTime deletedAt = member.getDeletedAt();

		return new DeleteMemberUseCaseResponse(member.getId(), deletedAt);
	}
}
