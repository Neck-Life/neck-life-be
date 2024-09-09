package com.necklife.api.web.dto.request.member;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class DeleteMemberBody {

	String withDrawReason;
}
