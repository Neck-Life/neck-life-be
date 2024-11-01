package com.necklife.api.repository.member;

import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.entity.member.OauthProvider;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository
		extends MongoRepository<MemberEntity, String>, MemberRepositoryCustom {

	Optional<MemberEntity> findByEmailAndOauthProviderAndDeletedAtIsNull(
			String email, OauthProvider oauthProvider);

	List<MemberEntity> findAllByTimeZone(String timeZone);

	@Query("{'timeZone': {$regex: '^America/'}}")
	List<MemberEntity> findAllByAmericanTimeZones();

	// For American time zones - Korean users (ko-KR)
	@Query(
			"{'timeZone': {$regex: '^America/'}, 'language': 'ko-KR', 'deletedAt': { $exists: false }}")
	List<MemberEntity> findAllByAmericanTimeZonesWithKorean();

	// For American time zones - Non-Korean users (any language except ko-KR)
	@Query(
			"{'timeZone': {$regex: '^America/'}, 'language': { $ne: 'ko-KR' }, 'deletedAt': { $exists: false }}")
	List<MemberEntity> findAllByAmericanTimeZonesWithEnglish();

	// For Asian time zones - Korean users (ko-KR)
	@Query("{'timeZone': {$regex: '^Asia/'}, 'language': 'ko-KR', 'deletedAt': { $exists: false }}")
	List<MemberEntity> findAllByAsianTimeZonesWithKorean();

	// For Asian time zones - Non-Korean users (any language except ko-KR)
	@Query(
			"{'timeZone': {$regex: '^Asia/'}, 'language': { $ne: 'ko-KR' }, 'deletedAt': { $exists: false }}")
	List<MemberEntity> findAllByAsianTimeZonesWithEnglish();
}
