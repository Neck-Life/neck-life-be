package com.necklife.api.repository.history;

import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.entity.history.RawHistoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RangeRawHistoryRepository {
    private final MongoTemplate mongoTemplate;



    public RawHistoryEntity findByTimestampRange(String MemberId, LocalDateTime start, LocalDateTime end) {
        Query query = new Query();
        query.addCriteria(Criteria.where("member.id").is(MemberId));
        query.addCriteria(Criteria.where("date").is(start.toLocalDate()));
        query.addCriteria(Criteria.where("rawData.timestamp")
                .gte(start)
                .lt(end));

        query.fields().include("rawData").exclude("_id"); // _id 필드 제외 시 명시적으로 제외 가능

        return  mongoTemplate.findOne(query, RawHistoryEntity.class);
    }
}
