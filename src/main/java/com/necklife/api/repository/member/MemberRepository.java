package com.necklife.api.repository.member;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.member.OauthProvider;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

	Optional<MemberEntity> findByEmailAndOauthProviderAndDeletedAtIsNull(
			String email, OauthProvider oauthProvider);
}
