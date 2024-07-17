package com.necklife.api.service;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.member.OauthProvider;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.repository.member.dto.response.PostMemberRepoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService {

    private final MemberRepository memberRepository;

    public PostMemberRepoResponse execute(String email, String password) {
        MemberEntity member =  memberRepository.findByEmailAndOauthProvider(email, OauthProvider.valueOf("NONE"))
                .orElseGet(() -> {
                    MemberEntity newMember = MemberEntity.builder()
                            .email(email)
                            .password(password)
                            .isSocial(false)
                            .oauthProvider(OauthProvider.valueOf("NONE"))
                            .build();

                    newMember.unpaid();


                    return memberRepository.save(newMember);
                });

        return PostMemberRepoResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .provider(member.getOauthProvider().toString())
                .status(member.getStatus().name())
                .build();


    }

}
