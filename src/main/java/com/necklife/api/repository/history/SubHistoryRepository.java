package com.necklife.api.repository.history;

import com.necklife.api.entity.history.SubHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;

public interface SubHistoryRepository extends JpaRepository<SubHistoryEntity, Long>,JDBCBulkSubHistoryRepository {


}
