package com.necklife.api.service.goal;

import com.necklife.api.entity.goal.GoalEntity;
import com.necklife.api.repository.goal.GoalRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetActiveGoalService {

	private final GoalRepository goalRepository;

	public Optional<GoalEntity> execute(String memberId, LocalDateTime dateTime) {

		return goalRepository.findActiveGoalForMemberAtDate(memberId, dateTime);
	}
}
