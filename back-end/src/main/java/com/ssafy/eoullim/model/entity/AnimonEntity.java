package com.ssafy.eoullim.model.entity;

import com.ssafy.eoullim.model.Animon;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "animon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnimonEntity extends BaseEntity {
  @Id
  @Column(name = "animon_id", columnDefinition = "INT UNSIGNED")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 20)
  private String name;

  @Column(nullable = false)
  private String headImagePath;

  @Column(nullable = false)
  private String bodyImagePath;

  @Builder
  public AnimonEntity(Long id, String name, String headImagePath, String bodyImagePath) {
    this.id = id;
    this.name = name;
    this.headImagePath = headImagePath;
    this.bodyImagePath = bodyImagePath;
  }

  public static AnimonEntity of(Animon animon) {
    return AnimonEntity.builder()
        .id(animon.getId())
        .name(animon.getName())
        .headImagePath(animon.getHeadImagePath())
        .bodyImagePath(animon.getBodyImagePath())
        .build();
  }
}
