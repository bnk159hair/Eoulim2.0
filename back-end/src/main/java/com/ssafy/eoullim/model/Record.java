package com.ssafy.eoullim.model;

import com.ssafy.eoullim.model.entity.RecordEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Record {
    private Long id;
    private String videoPath;
    private String participantName;
    private String participantSchool;
    private String participantAnimonName;

    public static Record fromEntity(RecordEntity entity) {
        return new Record(
                entity.getId(),
                entity.getVideoPath(),
                entity.getParticipant().getName(),
                entity.getParticipant().getSchool(),
                entity.getParticipant().getAnimon().getName()
        );
    }

}
