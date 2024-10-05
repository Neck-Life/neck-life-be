package com.necklife.api.repository.history;

import com.necklife.api.entity.history.RawHistoryEntity;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawHistoryRepository extends MongoRepository<RawHistoryEntity, String> {

	Optional<RawHistoryEntity> findByMemberAndDate(String memberId, LocalDate date);
}
