package com.necklife.api.repository.history;

import com.necklife.api.entity.history.RawHistoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RawHistoryRepository extends MongoRepository<RawHistoryEntity, String> {}
