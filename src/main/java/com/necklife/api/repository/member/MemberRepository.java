package com.necklife.api.repository.member;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.member.OauthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long>{

    Optional<MemberEntity> findByEmailAndOauthProvider(String email, OauthProvider oauthProvider);
}
