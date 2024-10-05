package com.necklife.api.service.history;

import com.necklife.api.entity.history.RawHistoryEntity;
import com.necklife.api.entity.member.MemberEntity;
import com.necklife.api.repository.history.RawHistoryRepository;
import com.necklife.api.repository.member.MemberRepository;
import com.necklife.api.web.usecase.dto.response.history.PostRawHistoryResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SaveOnlyRawDataService {

	private final MemberRepository memberRepository;
	private final RawHistoryRepository rawHistoryRepository;

	public List<PostRawHistoryResponse> execute(String MemberId, List<Map<String, String>> rawData) {

		MemberEntity MemberEntity =
				memberRepository
						.findById(MemberId)
						.orElseThrow(() -> new IllegalArgumentException("없는 멤버입니다."));

		List<RawHistoryEntity> rawHistoryEntities = new ArrayList<>();

		Map<LocalDate, List<Map<String, String>>> subRawData = new HashMap<>();

		log.info("rawData: {}", rawData);

		for (Map<String, String> raw : rawData) {
			LocalDateTime timestamp = LocalDateTime.parse(raw.get("timestamp"));
			List<Map<String, String>> orDefault =
					subRawData.getOrDefault(timestamp.toLocalDate(), new ArrayList<>());
			orDefault.add(raw);
			subRawData.put(timestamp.toLocalDate(), orDefault);
		}

		for (Map.Entry<LocalDate, List<Map<String, String>>> entry : subRawData.entrySet()) {

			Optional<RawHistoryEntity> findRawHistory =
					rawHistoryRepository.findByMemberAndDate(MemberId, entry.getKey());

			if (findRawHistory.isPresent()) {
				RawHistoryEntity rawHistoryEntity = findRawHistory.get();
				rawHistoryEntity.getRawData().addAll(entry.getValue());
				log.info("rawHistoryEntity: {}", rawHistoryEntity);
				rawHistoryEntities.add(rawHistoryEntity);
			} else {
				rawHistoryEntities.add(
						RawHistoryEntity.builder()
								.member(MemberEntity)
								.date(entry.getKey())
								.rawData(entry.getValue())
								.build());
			}
		}

		List<RawHistoryEntity> savedRawHistoryEntity = rawHistoryRepository.saveAll(rawHistoryEntities);

		log.info("savedRawHistoryEntity: {}", savedRawHistoryEntity);

		List<PostRawHistoryResponse> responseList =
				savedRawHistoryEntity.stream()
						.map(
								rawHistoryEntity ->
										PostRawHistoryResponse.builder()
												.date(rawHistoryEntity.getDate())
												.rawData(rawHistoryEntity.getRawData())
												.build())
						.toList();

		return responseList;
	}
}
