package com.necklife.api.web.dto.request.member;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PostInquiryBody {

    @NotEmpty @NotNull
    String title;

    @NotEmpty @NotNull
    String content;
}
