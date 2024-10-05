package com.necklife.api.repository.member;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.member.PaymentEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends MongoRepository<PaymentEntity, String> {

	Optional<PaymentEntity> findByOrderByUpdatedAtDesc(MemberEntity member);
}
