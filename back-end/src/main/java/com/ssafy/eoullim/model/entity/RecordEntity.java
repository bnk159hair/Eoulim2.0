package com.ssafy.eoullim.model.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Entity
@Table(name="record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecordEntity extends BaseEntity {
    @Id
    @Column(name = "record_id", columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String videoPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="master_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChildEntity master;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="participant_id")    // 상대방 아이가 삭제 되면? -> null로
    private ChildEntity participant;

    @Builder
    public RecordEntity(String videoPath, ChildEntity master, ChildEntity participant) {
        this.videoPath = videoPath;
        this.master = master;
        this.participant = participant;
    }

    public static RecordEntity of(String videoPath, ChildEntity master, ChildEntity participant){
        return RecordEntity.builder()
                .videoPath(videoPath)
                .master(master)
                .participant(participant)
                .build();
    }

}
