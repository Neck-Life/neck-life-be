package com.necklife.api.web.usecase;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.member.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExistMemberService {

	private final MemberRepository memberRepository;

	public boolean checkMemberExists(Long id) {

		Optional<MemberEntity> findMember = memberRepository.findById(id);
		return findMember.isPresent();
	}
}
