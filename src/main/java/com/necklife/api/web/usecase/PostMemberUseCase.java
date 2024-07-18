package com.necklife.api.web.usecase;

import com.necklife.api.repository.member.dto.response.PostMemberRepoResponse;
import com.necklife.api.service.Oauth2UserService;
import com.necklife.api.web.usecase.dto.response.PostMemberUseCaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostMemberUseCase {

    private final Oauth2UserService oauth2UserService;

    @Transactional(readOnly = false)
    public PostMemberUseCaseResponse execute(String code,String provider) {

        PostMemberRepoResponse savedMember = oauth2UserService.findOrSaveMember(code, provider);// Dummy provider, replace with actual logic

        return PostMemberUseCaseResponse.builder()
                .id(savedMember.getId())
                .email(savedMember.getEmail())
                .provider(savedMember.getProvider())
                .status(savedMember.getStatus()).build();
    }
}