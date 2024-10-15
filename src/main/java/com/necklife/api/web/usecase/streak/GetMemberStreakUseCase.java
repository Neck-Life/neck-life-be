package com.necklife.api.web.usecase.streak;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.streak.StreakEntity;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.service.streak.GetStreakService;
import com.necklife.api.web.usecase.dto.response.streak.StreakResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetMemberStreakUseCase {

	private final GetStreakService getStreakService;
	private final MemberRepository memberRepository;

	public StreakResponse execute(String memberId) {

		Optional<MemberEntity> findMember = memberRepository.findById(memberId);

		if (findMember.isPresent()) {
			StreakEntity execute = getStreakService.execute(findMember.get());
			return StreakResponse.builder()
					//					.lastGoalStreakDate(execute.getLastGoalStreakDate())
					.currentGoalStreak(execute.getCurrentGoalStreak())
					.maxGoalStreak(execute.getMaxGoalStreak())
					.lastHistoryStreakDate(execute.getLastHistoryStreakDate())
					.currentHistoryStreak(execute.getCurrentHistoryStreak())
					.maxHistoryStreak(execute.getMaxHistoryStreak())
					.build();

		} else {
			throw new IllegalArgumentException("없는 멤버 입니다.");
		}
	}
}
