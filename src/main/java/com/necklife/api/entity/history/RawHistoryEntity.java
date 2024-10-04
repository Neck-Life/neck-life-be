package com.necklife.api.entity.history;

import com.necklife.api.entity.member.MemberEntity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Document(collection = "raw_history_summary")
public class RawHistoryEntity {

    @Id
    private String id;

    @DBRef
    private MemberEntity member;

    @Indexed
    private LocalDate date;

    @DBRef
    private HistorySummaryEntity historySummaryEntity;


    private List<Map<String, String>> rawData;





    @CreatedDate
    @NotNull
    private LocalDateTime createdAt;

    @LastModifiedDate
    @NotNull private LocalDateTime updatedAt;








}
