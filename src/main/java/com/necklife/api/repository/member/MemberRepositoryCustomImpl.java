package com.necklife.api.repository.member;

import com.necklife.api.entity.member.MemberEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

	private final MongoTemplate mongoTemplate;

	public MemberRepositoryCustomImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public Optional<MemberEntity> updateLastLoginAtById(String id, LocalDateTime lastLoginAt) {
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update().set("lastLoginAt", lastLoginAt);

		mongoTemplate.updateFirst(query, update, MemberEntity.class);

		// Optional을 반환하도록 수정
		MemberEntity updatedMember = mongoTemplate.findOne(query, MemberEntity.class);
		return Optional.ofNullable(updatedMember);
	}

	public Optional<MemberEntity> updateLastLoginTimeZoneAndLanguageAtById(
			String id, LocalDateTime lastLoginAt, String timeZone, String language) {
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update().set("lastLoginAt", lastLoginAt);
		update.set("timeZone", timeZone);
		update.set("language", language);

		mongoTemplate.updateFirst(query, update, MemberEntity.class);

		// Optional을 반환하도록 수정
		MemberEntity updatedMember = mongoTemplate.findOne(query, MemberEntity.class);
		return Optional.ofNullable(updatedMember);
	}

	public Optional<MemberEntity> updateFCMToken(String id, String notificationToken) {
		Query query = new Query(Criteria.where("id").is(id));
		Update update = new Update().set("notificationToken", notificationToken);

		mongoTemplate.updateFirst(query, update, MemberEntity.class);

		// Optional을 반환하도록 수정
		MemberEntity updatedMember = mongoTemplate.findOne(query, MemberEntity.class);
		return Optional.ofNullable(updatedMember);
	}
}
