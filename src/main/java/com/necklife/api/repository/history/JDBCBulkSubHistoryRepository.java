package com.necklife.api.repository.history;


import com.necklife.api.entity.history.SubHistoryEntity;

import java.util.List;

public interface JDBCBulkSubHistoryRepository {


    public void bulkInsert(Long HistoryId,List<SubHistoryEntity> subHistoryEntities);


}
