package com.necklife.api.entity.history;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Builder
@Table(name = "sub_history")
public class SubHistoryEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private HistoryEntity history;

    @Column(nullable = false)
    private Date changedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PoseStatus poseStatus;

}
