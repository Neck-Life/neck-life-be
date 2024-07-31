package com.necklife.api.web.usecase.history;

import com.necklife.api.entity.history.SubHistoryEntity;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.web.usecase.dto.request.history.PostHistoryRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class PostHistoryUseCase {

	private final MemberRepository memberRepository;

	@Transactional
	public void execute(PostHistoryRequest postHistoryRequest) {
		Long requestMemberId = postHistoryRequest.memberId();

		checkExistMember(requestMemberId);





	}

	private void checkExistMember(Long requestMemberId) {
		if (!memberRepository.existsById(requestMemberId)) {
			throw new IllegalArgumentException("없는 멤버 입니다.");
		}
	}

}
