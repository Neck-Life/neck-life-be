package com.necklife.api.repository.member;

import com.necklife.api.entity.member.WithDrawReason;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WithDrawReasonRepository extends MongoRepository<WithDrawReason, String> {
}


