package com.necklife.api.web.usecase;

import com.necklife.api.web.usecase.dto.response.GetMemberTokenDetailUseCaseResponse;
import org.springframework.stereotype.Service;

@Service
public class GetMemberTokenDetailUseCase {

    public GetMemberTokenDetailUseCaseResponse execute(Long id) {
        // Implementation of the use case to get member token details
        return new GetMemberTokenDetailUseCaseResponse(id);
    }
}