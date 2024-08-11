package com.necklife.api.web.usecase.goal;

import com.necklife.api.entity.goal.GoalEntity;
import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.goal.GoalRepository;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.web.usecase.dto.response.goal.GoalResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetGoalsUseCase {

	private final GoalRepository goalRepository;
	private final MemberRepository memberRepository;

	public GoalResponse execute(String memberId) {

		// todo check로직 빼기
		Optional<MemberEntity> byId = memberRepository.findById(memberId);

		if (!byId.isPresent()) {
			throw new NotFoundException("Member not found");
		}

		LocalDateTime now = LocalDateTime.now();

		Optional<GoalEntity> activeGoalForMemberAtDate =
				goalRepository.findActiveGoalForMemberAtDate(memberId, now);

		if (activeGoalForMemberAtDate.isPresent()) {
			GoalEntity goalEntity = activeGoalForMemberAtDate.get();
			return GoalResponse.builder()
					.goals(
							goalEntity.getGoals().stream()
									.map(
											goalDetail ->
													GoalResponse.GoalDetailResponseDTO.builder()
															.order(goalDetail.getOrders())
															.type(goalDetail.getType())
															.description(
																	goalDetail.getType().getDescription(goalDetail.getTargetValue()))
															.targetValue(goalDetail.getTargetValue())
															.build())
									.toList())
					.build();
		} else {
			return GoalResponse.builder().goals(new ArrayList<>()).build();
		}
	}
}
