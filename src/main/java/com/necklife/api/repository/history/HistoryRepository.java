package com.necklife.api.repository.history;

import com.necklife.api.entity.history.HistoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

public interface HistoryRepository extends MongoRepository<HistoryEntity, String> {



    List<HistoryEntity> findByMemberIdAndYearAndMonth(String memberId, int year, int month);

    List<HistoryEntity> findByMemberIdAndYear(String memberId, int year);

}
