package com.ssafy.eoullim.model;

import com.ssafy.eoullim.model.entity.RecordEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class Record {
    private Long id;
    private String videoPath;
    private String name;
    private String school;
    private String animonName;

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
