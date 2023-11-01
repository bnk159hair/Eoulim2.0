package com.ssafy.eoullim.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity
@Table(name="record")
@NoArgsConstructor
public class RecordEntity extends BaseEntity {
    @Id
    @Column(name = "record_id")
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

    @Builder
    public RecordEntity(Long id, String videoPath, ChildEntity master, ChildEntity participant) {
        this.id = id;
        this.videoPath = videoPath;
        this.master = master;
        this.participant = participant;
    }

    public static RecordEntity of(String videoPath, ChildEntity master, ChildEntity participant){
        return new RecordEntity(
                null,
                videoPath,
                master,
                participant
        );
    }

}
