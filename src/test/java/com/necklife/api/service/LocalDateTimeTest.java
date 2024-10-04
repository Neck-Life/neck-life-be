package com.necklife.api.service;

import com.mongodb.client.MongoClient;
import com.necklife.api.config.MongoConfig;
import com.necklife.api.entity.history.HistorySummaryEntity;
import com.necklife.api.entity.history.PoseStatus;
import com.necklife.api.repository.history.HistorySummaryRepository;
import com.necklife.api.repository.history.RangeHistorySummaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringJUnitConfig(MongoConfig.class)
@ActiveProfiles("local")
@EnableAutoConfiguration
@Slf4j
@RunWith(SpringRunner.class)
@DataMongoTest
public class LocalDateTimeTest {

    @Autowired
    @Qualifier("mongoDbFactory")
    private MongoDatabaseFactory mongoDbFactory;

    @Test
    public void test() {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);

        String startStr = "2024-09-22T00:21:30";
        String endStr = "2024-09-23T00:25:30";

        RangeHistorySummaryRepository rangeHistorySummaryRepository = new RangeHistorySummaryRepository(mongoTemplate);
        ;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime start = LocalDateTime.now().minusDays(10);
        LocalDateTime end = LocalDateTime.now();
        String MemberId = "66e19500a5f9ac1d81f7aef4";

        long startTime = System.nanoTime();

        List<HistorySummaryEntity> historySummaryEntities = rangeHistorySummaryRepository.findByTimestampRange(MemberId, start);

        long endTime = System.nanoTime();
        // 경과 시간 계산 (나노초 단위)
        long duration = endTime - startTime;

        // 나노초를 밀리초로 변환
        double milliseconds = (double) duration / 1_000_000.0;

        for (   HistorySummaryEntity entity : historySummaryEntities) {
            Map<LocalDateTime, PoseStatus> localDateTimePoseStatusMap = filterPoseStatusByTimeRange(entity, start, end);

        }


        // 결과 출력
        System.out.println("Elapsed time: " + milliseconds + " ms");

    }

    public Map<LocalDateTime, PoseStatus> filterPoseStatusByTimeRange(HistorySummaryEntity entity, LocalDateTime start, LocalDateTime end) {



            Map<LocalDateTime, PoseStatus> poseStatusMap = entity.getTotalPoseStatusMap();

            // 필터링된 map을 저장할 새 map
            Map<LocalDateTime, PoseStatus> filteredMap = new HashMap<>();

            // map의 키(시간)들을 필터링
            for (Map.Entry<LocalDateTime,PoseStatus> entry  : poseStatusMap.entrySet()) {
                LocalDateTime timestamp = entry.getKey();


                if (timestamp.isAfter(start) && timestamp.isBefore(end) ) {
                    filteredMap.put(timestamp, entry.getValue());
                }
            }

            // 필터링된 결과를 엔티티에 다시 저장


        return filteredMap;
    }


}
