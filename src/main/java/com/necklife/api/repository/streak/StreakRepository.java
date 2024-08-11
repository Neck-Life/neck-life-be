package com.necklife.api.repository.streak;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.streak.StreakEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StreakRepository extends MongoRepository<StreakEntity, String> {
	Optional<StreakEntity> findByMember(MemberEntity member);
}
