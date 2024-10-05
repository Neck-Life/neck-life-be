package com.necklife.api.repository.member;

import com.necklife.api.entity.member.WithDrawReason;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithDrawReasonRepository extends MongoRepository<WithDrawReason, String> {}
