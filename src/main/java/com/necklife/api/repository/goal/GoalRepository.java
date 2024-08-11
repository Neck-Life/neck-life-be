package com.necklife.api.repository.goal;

import com.necklife.api.entity.goal.GoalEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface GoalRepository extends MongoRepository<GoalEntity, String> {

    @Query("{ 'memberId': ?0, 'effectiveFrom': { $lte: ?1 }, 'effectiveTo': { $eq: null } }")
    Optional<GoalEntity> findActiveGoalForMemberAtDate(String memberId, LocalDateTime dateTime);
}
