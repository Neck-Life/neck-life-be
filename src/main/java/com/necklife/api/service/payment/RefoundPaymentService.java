package com.necklife.api.service.payment;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.member.PaymentEntity;
import com.necklife.api.repository.member.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefoundPaymentService {

    private final PaymentRepository paymentRepository;

    public void execute(MemberEntity member, LocalDateTime dateTime,String refoundReason, Double won) {

        Optional<PaymentEntity> recentUpdate = paymentRepository.findByTopOrderByUpdatedAtDesc(member);
        LocalDateTime now = LocalDateTime.now();

        //이전 결제 정보가 없는 경우, 또는 이미 만료한 경우, 또는 결제상태가 없는 경우
        if (recentUpdate.isEmpty() ||recentUpdate.get().getEndAt().isBefore(now) || !recentUpdate.get().getStatus().equals("PAID") ) {
            throw new IllegalArgumentException("환불을 진행할 수 없습니다.");

        }else if(recentUpdate.get().getEndAt().isBefore(now)){
            paymentRepository.save(PaymentEntity.builder()
                    .member(member)
                    .updatedAt(dateTime)
                    .endAt(now)
                    .refoundWon(won)
                    .status("UNPAID")
                    .Refoundreason(refoundReason)
                    .build());

        }else{
            throw new IllegalArgumentException("환불을 진행 할 수 없습니다.");
        }
    }
}
