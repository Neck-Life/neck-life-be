package com.necklife.api.web.usecase;

import com.necklife.api.web.usecase.dto.response.GetMemberDetailUseCaseResponse;
import org.springframework.stereotype.Service;

@Service
public class GetMemberDetailUseCase {

    public GetMemberDetailUseCaseResponse execute(Long id) {
        // Implementation of the use case to get member details
        return new GetMemberDetailUseCaseResponse(
                1L,
                "email",
                "certificationSubject",
                "status"
        );
    }


}