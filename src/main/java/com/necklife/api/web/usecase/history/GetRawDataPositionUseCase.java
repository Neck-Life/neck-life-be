package com.necklife.api.web.usecase.history;

import com.necklife.api.entity.history.RawHistoryEntity;
import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.history.RangeRawHistoryRepository;
import com.necklife.api.repository.history.RawHistoryRepository;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.web.usecase.dto.response.history.GetRawHistoryResponse;
import com.necklife.api.web.usecase.dto.response.history.PostRawHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetRawDataPositionUseCase {

    private final RangeRawHistoryRepository rangeRawHistoryRepository;
    private final MemberRepository memberRepository;

    public GetRawHistoryResponse execute(String MemberId, LocalDateTime rawTimestamp) {

        memberRepository.findById(MemberId).orElseThrow(() -> new IllegalArgumentException("없는 멤버입니다."));

        List<Map<String, String>> rawData = new ArrayList<>();

        LocalDateTime start = rawTimestamp.minusSeconds(3);
        LocalDateTime end = rawTimestamp.plusSeconds(3);

        if (start.isBefore(rawTimestamp.minusDays(1))) {
            rawData.addAll(rangeRawHistoryRepository.findByTimestampRange(MemberId, end.toLocalDate().atStartOfDay(), end).getRawData());
        }


        rawData.addAll(rangeRawHistoryRepository.findByTimestampRange(MemberId, start, end).getRawData());

        LocalDateTime startTime = LocalDateTime.parse(rawData.get(0).get("timestamp"));
        for (Map<String, String> raw : rawData) {
            LocalDateTime timestamp = LocalDateTime.parse(raw.get("timestamp"));
            raw.remove("timestamp");
            long milliseconds = java.time.Duration.between(startTime, timestamp).toMillis();
            raw.put("milliseconds", String.valueOf(milliseconds));
        }

        return GetRawHistoryResponse.builder()
                .rawData(rawData)
                .build();

    }

}
