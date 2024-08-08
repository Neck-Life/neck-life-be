package com.necklife.api.web.usecase.member;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.member.MemberStatus;
import com.necklife.api.entity.member.PaymentEntity;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.service.payment.AddPaymentService;
import com.necklife.api.web.usecase.dto.response.member.PostPaymentUseCaseResponse;
import com.necklife.api.web.usecase.dto.response.member.PostRefoundPaymentUseCaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostPaymentUseCase {

    private final MemberRepository memberRepository;
    private final AddPaymentService addPaymentService;

    public PostPaymentUseCaseResponse execute(String memberId, LocalDateTime date,Long months, Double won) {

        Optional<MemberEntity> findMember = memberRepository.findById(memberId);

        validatePayment(findMember);

        PaymentEntity savedPayment = addPaymentService.execute(findMember.get(), date, months, won);



        return PostPaymentUseCaseResponse.builder()
                .memberId(memberId)
                .status(savedPayment.getStatus())
                .serviceEndDate(savedPayment.getEndAt())
                .build();
    }

    private static void validatePayment(Optional<MemberEntity> findMember) {
        if(findMember.isEmpty()) {
            throw new NotFoundException("Member Not Found");
        }

        MemberEntity member = findMember.get();

        //todo Excepiton분리
        if (member.getStatus() != MemberStatus.UNPAID) {
            throw new IllegalArgumentException("결제를 진행할 수 없습니다.");
        }
    }

}
