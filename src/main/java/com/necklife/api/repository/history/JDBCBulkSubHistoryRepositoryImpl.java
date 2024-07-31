package com.necklife.api.repository.history;

import com.necklife.api.entity.history.SubHistoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;


@Transactional
@RequiredArgsConstructor
public class JDBCBulkSubHistoryRepositoryImpl implements JDBCBulkSubHistoryRepository{

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void bulkInsert(Long historyId, List<SubHistoryEntity> subHistoryEntities) {
        String sql = "INSERT INTO sub_history(historyId, changeAt,changeType) " +
                "VALUES (?,?,?)";

        int size = subHistoryEntities.size();
        jdbcTemplate.batchUpdate(sql, subHistoryEntities, size,
                (PreparedStatement ps, SubHistoryEntity subHistoryEntity) -> {
                    ps.setLong(1, historyId);
                    ps.setDate(2, new java.sql.Date(subHistoryEntity.getChangedAt().getTime()));
                    ps.setString(3, String.valueOf(subHistoryEntity.getPoseStatus()));
                }
        );
    }

}
