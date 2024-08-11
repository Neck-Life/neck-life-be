package com.necklife.api.web.usecase.steak;


import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.streak.StreakEntity;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.service.streak.GetStreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetMemberStreakUseCase {

    private final GetStreakService getStreakService;
    private final MemberRepository memberRepository;
    public void execute(String memberId) {


        Optional<MemberEntity> findMember = memberRepository.findById(memberId);

        if(findMember.isPresent()) {
            StreakEntity execute = getStreakService.execute(findMember.get());

        }else {
            throw new IllegalArgumentException("없는 멤버 입니다.");
        }

    }

}
