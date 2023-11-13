package com.ssafy.eoullim.model;

import com.ssafy.eoullim.model.entity.ChildAnimonEntity;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChildAnimon {
  private Long id;
  private Child child;
  private Animon animon;

  public static ChildAnimon fromEntity(ChildAnimonEntity childAnimonEntity) {
    return ChildAnimon.builder()
        .id(childAnimonEntity.getId())
        .child(Child.fromEntity(childAnimonEntity.getChild()))
        .animon(Animon.fromEntity(childAnimonEntity.getAnimon()))
        .build();
  }
}
