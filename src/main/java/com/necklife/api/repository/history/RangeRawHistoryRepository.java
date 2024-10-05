package com.necklife.api.repository.history;

import com.necklife.api.entity.history.RawHistoryEntity;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RangeRawHistoryRepository {
	private final MongoTemplate mongoTemplate;

	public RawHistoryEntity findByTimestampRange(
			String MemberId, LocalDateTime start, LocalDateTime end) {
		Query query = new Query();
		query.addCriteria(Criteria.where("memberId").is(MemberId));

		query.addCriteria(
				Criteria.where("rawData.timestamp").gte(start.toString()).lte(end.toString()));

		query.fields().include("rawData").exclude("_id"); // _id 필드 제외 시 명시적으로 제외 가능

		return mongoTemplate.findOne(query, RawHistoryEntity.class);
	}
}
