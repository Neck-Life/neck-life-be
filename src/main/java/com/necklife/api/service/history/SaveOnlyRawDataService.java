package com.necklife.api.service.history;

import com.necklife.api.entity.history.RawHistoryEntity;
import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.history.RawHistoryRepository;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.web.usecase.dto.response.history.PostRawHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SaveOnlyRawDataService {

    private final MemberRepository memberRepository;
    private final RawHistoryRepository rawHistoryRepository;


    public List<PostRawHistoryResponse> execute(String MemberId, List<Map<String, String>> rawData) {

        MemberEntity MemberEntity = memberRepository.findById(MemberId).orElseThrow(() -> new IllegalArgumentException("없는 멤버입니다."));

        List<RawHistoryEntity> rawHistoryEntities = new ArrayList<>();

        Map<LocalDate, List<Map<String,String>>> subRawData = new HashMap<>();

        for (Map<String, String> raw : rawData) {
            LocalDateTime timestamp = LocalDateTime.parse(raw.get("timestamp"));
            subRawData.getOrDefault(timestamp.toLocalDate(), new ArrayList<>()).add(raw);
        }

        for (Map.Entry<LocalDate, List<Map<String, String>>> entry : subRawData.entrySet()) {
            rawHistoryEntities.add(RawHistoryEntity.builder()
                    .member(MemberEntity)
                    .date(entry.getKey())
                    .rawData(entry.getValue())
                    .build());
        }

        List<RawHistoryEntity> savedRawHistoryEntity = rawHistoryRepository.saveAll(rawHistoryEntities);


        List<PostRawHistoryResponse> responseList = savedRawHistoryEntity.stream().map(
                rawHistoryEntity -> PostRawHistoryResponse.builder()
                        .date(rawHistoryEntity.getDate())
                        .rawData(rawHistoryEntity.getRawData())
                        .build()
        ).toList();

        return responseList;

    }
}
