package com.necklife.api.repository.history;

import com.necklife.api.entity.history.HistorySummaryEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface HistorySummaryRepository extends MongoRepository<HistorySummaryEntity, String> {

	List<HistorySummaryEntity> findAllByMemberId(String memberId);

	Optional<HistorySummaryEntity> findByMemberAndDate(String memberId, LocalDate date);

	// 특정 년도에 해당하는 데이터를 가져오는 쿼리
	List<HistorySummaryEntity> findByMemberIdAndDateBetweenOrderByDate(
			String memberId, LocalDate startDate, LocalDate endDate);

	// 가장 최근 데이터를 가져오는 쿼리
	Optional<HistorySummaryEntity> findTopByMemberIdOrderByDateDesc(String memberId);


//	@Query(value = "{ 'date': {'$gte': ?1, '$lte': ?2 } }", fields = "{'totalHistoryPoint': 1  }")
//	List<HistorySummaryEntity> findPointByMemberIdAndDate(String memberId, LocalDate start_At, LocalDate today);

	// field가 localDate 타입이므로 isoDate 변환이 필요함
	@Query(value = "{ 'memberId': ?0, 'date': {'$gte': ?1, '$lte': ?2 } }", fields = "{'totalHistoryPoint': 1, 'date': 1}")
	List<HistorySummaryEntity> findPointByMemberIdAndDate(String memberId, LocalDate start_At, LocalDate end_At);




}
