package com.necklife.api.service.streak;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.streak.StreakEntity;
import com.necklife.api.repository.streak.StreakRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetStreakService {

    private final StreakRepository streakRepository;

    public StreakEntity execute(MemberEntity member) {
        return streakRepository.findByMember(member)
                .orElseGet(() -> StreakEntity.builder()
                        .member(member)
                        .currentGoalStreak(0)
                        .maxGoalStreak(0)
                        .currentHistoryStreak(0)
                        .maxHistoryStreak(0)
                        .build());
    }

}
