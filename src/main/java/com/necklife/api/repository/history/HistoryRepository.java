package com.necklife.api.repository.history;

import com.necklife.api.entity.history.HistoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistoryRepository extends MongoRepository<HistoryEntity, String> {}
