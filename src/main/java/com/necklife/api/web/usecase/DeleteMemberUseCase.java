package com.necklife.api.web.usecase;

import com.necklife.api.web.usecase.dto.response.DeleteMemberUseCaseResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DeleteMemberUseCase {


    //todo 구현
        public DeleteMemberUseCaseResponse execute(Long memberId) {
            return new DeleteMemberUseCaseResponse(1L, LocalDateTime.now());
        }
}
