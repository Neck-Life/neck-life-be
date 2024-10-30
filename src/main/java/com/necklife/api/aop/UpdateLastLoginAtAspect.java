package com.necklife.api.aop;

import com.necklife.api.entity.member.MemberEntity;
import java.time.LocalDateTime;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UpdateLastLoginAtAspect {

	private final MongoTemplate mongoTemplate;

	public UpdateLastLoginAtAspect(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Around("@annotation(com.necklife.api.annotation.UpdateLastLoginAt) && args(memberId,..)")
	public Object updateLastLogin(ProceedingJoinPoint joinPoint, String memberId) throws Throwable {
		// 메서드 실행
		Object result = joinPoint.proceed();

		// MongoDB에서 lastLoginAt 필드만 업데이트
		Query query = new Query(Criteria.where("id").is(memberId));
		Update update = new Update().set("lastLoginAt", LocalDateTime.now());
		mongoTemplate.updateFirst(query, update, MemberEntity.class);

		return result;
	}
}
