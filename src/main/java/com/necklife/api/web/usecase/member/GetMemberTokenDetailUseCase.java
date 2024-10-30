package com.necklife.api.web.usecase.member;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.web.usecase.dto.request.member.GetMemberTokenDetailUseCaseRequest;
import com.necklife.api.web.usecase.dto.response.member.GetMemberTokenDetailUseCaseResponse;
import java.time.LocalDateTime;
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

	public GetMemberTokenDetailUseCaseResponse execute(GetMemberTokenDetailUseCaseRequest request) {

		Optional<MemberEntity> findMember = memberRepository.findById(request.getId());
		findMember.orElseThrow(() -> new NotFoundException("Member not found"));
		MemberEntity memberEntity = findMember.get();

		memberRepository.updateLastLoginTimeZoneAndLanguageAtById(
				memberEntity.getId(), LocalDateTime.now(), request.getTimeZone(), request.getLanguage());

		return new GetMemberTokenDetailUseCaseResponse(findMember.get().getId());
	}
}
