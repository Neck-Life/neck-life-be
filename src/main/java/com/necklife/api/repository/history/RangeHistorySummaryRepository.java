package com.necklife.api.repository.history;

import com.necklife.api.entity.history.HistorySummaryEntity;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RangeHistorySummaryRepository {

	private final MongoTemplate mongoTemplate;

	public List<HistorySummaryEntity> findByTimestampRange(String MemberId, LocalDateTime start) {
		Query query = new Query();
		query
				.addCriteria(Criteria.where("memberId").is(MemberId))
				.addCriteria(Criteria.where("date").gte(start.toInstant(ZoneOffset.UTC)));

		return mongoTemplate.find(query, HistorySummaryEntity.class);
	}

	public HistorySummaryEntity findByMemberIdAndDate(String memberId, LocalDateTime date) {
		Query query = new Query();
		query
				.addCriteria(Criteria.where("memberId").is(memberId))
				.addCriteria(Criteria.where("date").is(date));

		return mongoTemplate.findOne(query, HistorySummaryEntity.class);
	}
}
