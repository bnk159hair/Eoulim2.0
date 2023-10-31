package com.ssafy.eoullim.model;

import com.ssafy.eoullim.model.entity.AnimonEntity;
import com.ssafy.eoullim.model.entity.ChildEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Animon {

  private Integer id;
  private String headImagePath;
  private String bodyImagePath;
  private String name;

  public static Animon fromEntity(AnimonEntity entity) {
    return Animon.builder()
        .id(entity.getId())
        .name(entity.getName())
        .headImagePath(entity.getHeadImagePath())
        .bodyImagePath(entity.getBodyImagePath())
        .build();
  }
}
