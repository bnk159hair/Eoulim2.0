package com.ssafy.eoullim.model;

import com.ssafy.eoullim.model.entity.AnimonEntity;
import com.ssafy.eoullim.model.entity.ChildEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Animon {

  private Long id;
  private String maskImagePath;
  private String bodyImagePath;
  private String name;

  public static Animon fromEntity(AnimonEntity entity) {
    return Animon.builder()
        .id(entity.getId())
        .name(entity.getName())
        .maskImagePath(entity.getMaskImagePath())
        .bodyImagePath(entity.getBodyImagePath())
        .build();
  }
}
