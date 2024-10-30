package com.necklife.api.web.usecase.member;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.member.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostFCMUseCase {

	private final MemberRepository memberRepository;

	public void execute(String memberId, String fcmToken) {
		Optional<MemberEntity> findMember = memberRepository.findById(memberId);

		if (findMember.isEmpty()) {
			throw new IllegalArgumentException("Member not found");
		}

		memberRepository.updateFCMToken(memberId, fcmToken);
	}
}
