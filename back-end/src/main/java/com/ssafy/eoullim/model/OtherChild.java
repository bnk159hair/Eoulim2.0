package com.ssafy.eoullim.model;

import com.ssafy.eoullim.model.entity.ChildEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Date;
@Getter
@AllArgsConstructor
public class OtherChild {
    private Integer id;
    private String name;
    private Animon animon;
    public static OtherChild fromEntity(ChildEntity entity) {
        return new OtherChild(
                entity.getId(),
                entity.getName(),
                Animon.fromEntity(entity.getAnimon())
        );
    }
}

