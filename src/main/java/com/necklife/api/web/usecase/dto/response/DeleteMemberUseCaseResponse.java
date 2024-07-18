package com.necklife.api.web.usecase.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class DeleteMemberUseCaseResponse {
    private Long id;
    private LocalDateTime deletedAt;

}