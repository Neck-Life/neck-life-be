package com.necklife.api.service;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.repository.member.dto.response.PostMemberRepoResponse;
import com.necklife.api.service.oauth.AppleOauthService;
import com.necklife.api.service.oauth.GoogleOauthService;
import com.necklife.api.service.oauth.KaKaoOauthService;
import com.necklife.api.web.client.member.dto.SocialMemberData;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class Oauth2UserService {


    private final MemberRepository memberRepository;

    private final KaKaoOauthService kaKaoOauthService;
    private final GoogleOauthService googleOauthService;
    private final AppleOauthService appleOauthService;

    @Transactional(readOnly = false)
    public PostMemberRepoResponse findOrSaveMember(String id_token, String provider) {
        SocialMemberData socialMemberData;
        switch (provider) {
            case "google":
                socialMemberData = googleOauthService.execute(id_token);
                break;
            case "kakao":
                socialMemberData = kaKaoOauthService.execute(id_token);
                break;
            case "apple":
                socialMemberData = appleOauthService.execute(id_token);
                break;
            default:
                throw new RuntimeException("제공하지 않는 인증기관입니다.");
        }


        MemberEntity member = memberRepository.findByEmailAndOauthProvider(socialMemberData.getEmail(),socialMemberData.getProvider())
                .orElseGet(() -> {
                    MemberEntity newMember = MemberEntity.builder()
                            .email(socialMemberData.getEmail())
                            .isSocial(true)
                            .oauthProvider(socialMemberData.getProvider())
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
