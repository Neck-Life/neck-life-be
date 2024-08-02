package com.necklife.api.repository.member;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.member.OauthProvider;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends MongoRepository<MemberEntity, String> {

	Optional<MemberEntity> findByEmailAndOauthProviderAndDeletedAtIsNull(
			String email, OauthProvider oauthProvider);
}
