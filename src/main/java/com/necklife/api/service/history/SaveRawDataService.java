package com.necklife.api.service.history;

import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.entity.history.RawHistoryEntity;
import com.necklife.api.entity.member.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class SaveRawDataService {

    public RawHistoryEntity execute(MemberEntity memberEntity, HistorySummaryEntity historySummaryEntity, List<Map<String, String>> rawData) {

        return RawHistoryEntity.builder().member(memberEntity)
                .date(historySummaryEntity.getDate())
                .rawData(rawData)
                .build();

    }


}
