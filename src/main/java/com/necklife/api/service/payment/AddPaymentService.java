package com.necklife.api.service.payment;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.member.PaymentEntity;
import com.necklife.api.repository.member.PaymentRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddPaymentService {

	private final PaymentRepository paymentRepository;

	@Transactional
	public PaymentEntity execute(
			MemberEntity member, LocalDateTime dateTime, Long months, Double won) {

		Optional<PaymentEntity> recentUpdate = paymentRepository.findByOrderByUpdatedAtDesc(member);
		LocalDateTime now = LocalDateTime.now();


		// 이전 결제 정보가 없는 경우
		if (recentUpdate.isEmpty()) {
			member.paid();
			return paymentRepository.save(
					PaymentEntity.builder()
							.member(member)
							.updatedAt(dateTime)
							.endAt(dateTime.plusMonths(months))
							.paymentWon(won)
							.build());


			// 이전 결제가 있지만 만료 또는 추가 결제인 경우
		} else if (recentUpdate.get().getEndAt().isBefore(now)) {
			member.paid();
			return paymentRepository.save(
					PaymentEntity.builder()
							.member(member)
							.updatedAt(dateTime)
							.endAt(
									recentUpdate.get().getEndAt().isBefore(now)
											? now.plusMonths(months)
											: recentUpdate.get().getEndAt().plusMonths(months))
							.paymentWon(won)
							.build());

		} else {
			throw new IllegalArgumentException("결제를 진행 할 수 없습니다.");
		}
	}
}
