package com.necklife.api.web.usecase.member;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.member.MemberStatus;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.service.payment.RefoundPaymentService;
import com.necklife.api.web.usecase.dto.response.member.PostRefoundPaymentUseCaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostRefoundPaymentUseCase {

    private final MemberRepository memberRepository;
    private final RefoundPaymentService refoundPaymentService;

    @Transactional
    public PostRefoundPaymentUseCaseResponse execute(String memberId,LocalDateTime date, String reason,
                                                     Double refoundWon) {

        Optional<MemberEntity> findMember = memberRepository.findById(memberId);

        validateRefound(findMember);

        refoundPaymentService.execute(findMember.get(),date , reason, refoundWon);

        return PostRefoundPaymentUseCaseResponse.builder()
                .memberId(memberId)
                .status("UNPAID")
                .build();
    }

    private static void validateRefound(Optional<MemberEntity> findMember) {
        if(findMember.isEmpty()) {
            throw new NotFoundException("Member Not Found");
        }

        MemberEntity member = findMember.get();

        //todo Excepiton분리
        if (member.getStatus() != MemberStatus.PAID) {
            throw new IllegalArgumentException("환불을 진행할 수 없습니다.");
        }
    }
}
