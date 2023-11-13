package com.ssafy.eoullim.model;

import com.ssafy.eoullim.model.entity.FollowEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Follow {
  private Long id;
  private Child child;
  private Child followingChild;

  public static Follow fromEntity(FollowEntity followEntity) {
    return Follow.builder()
        .id(followEntity.getId())
        .child(Child.fromEntity(followEntity.getChild()))
        .followingChild(Child.fromEntity(followEntity.getFollowingChild()))
        .build();
  }
}
