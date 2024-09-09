package com.necklife.api.entity.member;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Document(collection = "inquiry")
public class InquiryEntity {

	private MemberEntity member;
	private String title;
	private String content;
}
