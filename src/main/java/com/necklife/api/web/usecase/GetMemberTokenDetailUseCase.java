package com.necklife.api.web.usecase;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.web.usecase.dto.response.GetMemberTokenDetailUseCaseResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMemberTokenDetailUseCase {

	private final MemberRepository memberRepository;

	public GetMemberTokenDetailUseCaseResponse execute(Long id) {

		Optional<MemberEntity> findMember = memberRepository.findById(id);
		findMember.orElseThrow(() -> new NotFoundException("Member not found"));

		return new GetMemberTokenDetailUseCaseResponse(findMember.get().getId());
	}
}
