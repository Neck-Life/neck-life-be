package com.necklife.api.entity.member;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Document(collection = "withDrawReason")
public class WithDrawReason {

    @DBRef
    private MemberEntity member;

    private String withDrawReason;




}
