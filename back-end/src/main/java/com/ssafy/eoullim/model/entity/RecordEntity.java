package com.ssafy.eoullim.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name="record_info")
@NoArgsConstructor
public class RecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String videoPath;

    @Column(nullable = false)
    private String guideSeq;

    @Column(nullable = false)
    private String timeline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="master_id", nullable = false)
    private ChildEntity master;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="participant_id", nullable = false)
    private ChildEntity participant;

    public RecordEntity(Integer id, String videoPath, ChildEntity master, ChildEntity participant, String guideSeq, String timeline) {
        this.id = id;
        this.videoPath = videoPath;
        this.master = master;
        this.participant = participant;
        this.guideSeq = guideSeq;
        this.timeline = timeline;
    }

    public static RecordEntity of(String videoPath, ChildEntity master, ChildEntity participant, String guideSeq, String timeline){
        return new RecordEntity(
                null,
                videoPath,
                master,
                participant,
                guideSeq,
                timeline
        );
    }

}
