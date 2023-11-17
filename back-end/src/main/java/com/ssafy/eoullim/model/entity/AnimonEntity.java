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
  private String maskImagePath;

  @Column(nullable = false)
  private String bodyImagePath;

  @Builder
  public AnimonEntity(Long id, String name, String maskImagePath, String bodyImagePath) {
    this.id = id;
    this.name = name;
    this.maskImagePath = maskImagePath;
    this.bodyImagePath = bodyImagePath;
  }

  public static AnimonEntity of(Animon animon) {
    return AnimonEntity.builder()
        .id(animon.getId())
        .name(animon.getName())
        .maskImagePath(animon.getMaskImagePath())
        .bodyImagePath(animon.getBodyImagePath())
        .build();
  }
}
