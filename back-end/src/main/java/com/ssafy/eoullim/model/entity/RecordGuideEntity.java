package com.ssafy.eoullim.model.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Entity
@Table(name="record_guide")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordGuideEntity extends BaseEntity{
    @Id
    @Column(name = "record_guide_id", columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RecordEntity record;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id", nullable = false)
    private GuideEntity guide;

    @Column(nullable = false)
    private Integer sequence;

    @Column(nullable = false)
    private String timeline;

    @Builder
    public RecordGuideEntity(RecordEntity record, GuideEntity guide, Integer sequence, String timeline) {
        this.record = record;
        this.guide = guide;
        this.sequence = sequence;
        this.timeline = timeline;
    }
}
