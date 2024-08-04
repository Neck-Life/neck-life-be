package com.necklife.api.repository.history;

import com.necklife.api.entity.history.HistoryEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistoryRepository extends MongoRepository<HistoryEntity, String> {

	List<HistoryEntity> findByMemberIdAndYearAndMonth(String memberId, int year, int month);

	List<HistoryEntity> findByMemberIdAndYear(String memberId, int year);
}
