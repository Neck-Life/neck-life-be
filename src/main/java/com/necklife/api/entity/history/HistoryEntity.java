package com.necklife.api.entity.history;

import com.necklife.api.entity.member.MemberEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Builder
@Table(name = "history")
public class HistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private MemberEntity member;

    @OneToMany(mappedBy = "sub_history_id")
    private List<SubHistoryEntity> subHistory;

    @Column(nullable = false)
    private Date startAt;

    @Column(nullable = false)
    private Date endAt;


    public HistoryEntity updateSubHistory(List<SubHistoryEntity> subHistory) {
        this.subHistory = subHistory;
        return this;
    }


}
