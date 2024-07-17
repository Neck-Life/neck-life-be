package com.necklife.api.web.usecase;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.web.usecase.dto.response.GetMemberDetailUseCaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMemberDetailUseCase {

    private final MemberRepository memberRepository;

    public GetMemberDetailUseCaseResponse execute(Long id) {

        Optional<MemberEntity> findMember = memberRepository.findById(id);
        findMember.orElseThrow(() -> new NotFoundException("Member not found"));
        MemberEntity member = findMember.get();

        return new GetMemberDetailUseCaseResponse(
                member.getId(),
                member.getEmail(),
                member.getOauthProvider().toString(),
                member.getStatus().toString());
    }


}