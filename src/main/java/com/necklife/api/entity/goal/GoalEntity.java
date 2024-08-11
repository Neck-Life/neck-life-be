package com.necklife.api.entity.goal;

import com.necklife.api.entity.member.MemberEntity;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Document(collection = "goal")
public class GoalEntity {

    @Id
    private String id;

    @DBRef
    private MemberEntity member;

    private List<GoalDetail> goals; // 여러 목표를 저장하는 배열

    @CreatedDate
    @Indexed
    private LocalDateTime effectiveFrom; // 이 목표가 유효하기 시작한 시각
    private LocalDateTime effectiveTo;

    public GoalEntity updateEffectiveTo(LocalDateTime effectiveTo) {
        this.effectiveTo = effectiveTo;
        return this;
    }


    @Getter
    @Builder
    static public class GoalDetail {

        private Integer orders; // 목표의 순서 (예: 1, 2, 3)
        private GoalType type; // 목표의 유형 (예: "measurement", "score")
        private String description; // 목표 설명 (예: "하루에 3시간 이상 측정하기")
        private Double targetValue; // 목표의 타겟 값 (예: 3시간 또는 80점)


        public void setOrders(int orders) {
            this.orders = orders;
        }
    }


}
