package com.necklife.api.repository.member;

import com.necklife.api.entity.member.PaymentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface PaymentRepository extends MongoRepository<PaymentEntity, String> {


    Optional<PaymentEntity> findTopPaymentAtByMemberAndOrderByPaymentAtDesc();
    Optional<PaymentEntity> findTopRefoundAtByMemberAndOrderByRefoundAtDesc();


}
