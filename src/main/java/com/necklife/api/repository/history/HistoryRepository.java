package com.necklife.api.repository.history;

import com.necklife.api.entity.history.HistoryEntity;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface HistoryRepository extends MongoRepository<HistoryEntity, String> {

	@Query(value = "{ 'date': { $in: ?0 } }", fields = "{ 'start_at': 1, '_id': 0 }")
	HashSet<HistoryEntity> findStartAtByDateList(Set<LocalDate> dateList);
}
