package com.necklife.api.web.usecase.history;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.service.history.SaveHistoryService;
import com.necklife.api.web.usecase.dto.request.history.PostHistoryRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class PostHistoryUseCase {

	private final MemberRepository memberRepository;
	private final SaveHistoryService saveHistoryService;

	@Transactional
	public void execute(PostHistoryRequest postHistoryRequest) {
		String requestMemberId = postHistoryRequest.memberId();

		checkExistMember(requestMemberId);

		MemberEntity findMember = memberRepository.findById(postHistoryRequest.memberId()).get();

		saveHistoryService.execute(
				findMember,
				postHistoryRequest.subHistories());
	}

	private void checkExistMember(String requestMemberId) {
		if (!memberRepository.existsById(requestMemberId)) {
			throw new IllegalArgumentException("없는 멤버 입니다.");
		}
	}
}
