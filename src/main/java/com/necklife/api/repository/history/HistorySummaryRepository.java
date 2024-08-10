package com.necklife.api.repository.history;

import com.necklife.api.entity.history.HistorySummaryEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistorySummaryRepository extends MongoRepository<HistorySummaryEntity, String> {

	List<HistorySummaryEntity> findAllByMemberId(String memberId);

	Optional<HistorySummaryEntity> findByMemberAndDate(String memberId, LocalDate date);

	// 특정 년도에 해당하는 데이터를 가져오는 쿼리
	List<HistorySummaryEntity> findByMemberIdAndDateBetweenOrderByDate(
			String memberId, LocalDate startDate, LocalDate endDate);
}
