package com.necklife.api.repository.history;

import com.necklife.api.entity.history.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {



    List<HistoryEntity> findAllByStartAtBetween(Long memberId, Date startDate, Date endDate);
}
