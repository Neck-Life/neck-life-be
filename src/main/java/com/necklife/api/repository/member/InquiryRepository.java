package com.necklife.api.repository.member;

import com.necklife.api.entity.member.InquiryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InquiryRepository extends MongoRepository<InquiryEntity, String> {
}
