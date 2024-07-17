package com.necklife.api.web.usecase;

import com.necklife.api.web.usecase.dto.response.PostMemberUseCaseResponse;
import org.springframework.stereotype.Service;

@Service
public class PostMemberUseCase {

    public PostMemberUseCaseResponse execute(String code) {
        // Implementation of the use case to post a new member
        Long id = 1L; // Dummy id, replace with actual logic
        return new PostMemberUseCaseResponse(id, "이메일", "제공자", "상태");
    }
}