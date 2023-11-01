package com.ssafy.eoullim.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name="record_info")
@NoArgsConstructor
public class RecordEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String videoPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="master_id", nullable = false)
    private ChildEntity master;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="participant_id", nullable = false)
    private ChildEntity participant;

    public RecordEntity(Long id, String videoPath, ChildEntity master, ChildEntity participant) {
        this.id = id;
        this.videoPath = videoPath;
        this.master = master;
        this.participant = participant;
    }

    public static RecordEntity of(String videoPath, ChildEntity master, ChildEntity participant, String guideSeq, String timeline){
        return new RecordEntity(
                null,
                videoPath,
                master,
                participant
        );
    }

}
