package com.necklife.api.repository.member;

import com.necklife.api.entity.member.MemberEntity;
import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberRepositoryCustom {
	Optional<MemberEntity> updateLastLoginAtById(String id, LocalDateTime lastLoginAt);

	Optional<MemberEntity> updateLastLoginTimeZoneAndLanguageAtById(
			String id, LocalDateTime lastLoginAt, String timeZone, String language);

	Optional<MemberEntity> updateFCMToken(String id, String notificationToken);
}
