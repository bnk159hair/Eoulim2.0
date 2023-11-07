package com.ssafy.eoullim.model.entity;

import com.ssafy.eoullim.model.Animon;
import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.ChildAnimon;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "child_animon")
public class ChildAnimonEntity extends BaseEntity {
  @Id
  @Column(name = "child_animon_id", columnDefinition = "INT UNSIGNED")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "child_id")
  private ChildEntity child;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "animon_id")
  private AnimonEntity animon;

  @Builder
  public ChildAnimonEntity(Long id, Child child, Animon animon) {
    this.id = id;
    this.child = ChildEntity.of(child);
    this.animon = AnimonEntity.of(animon);
  }

  public static ChildAnimonEntity of(ChildAnimon childAnimon) {
     return ChildAnimonEntity.builder()
             .id(childAnimon.getId())
             .child(childAnimon.getChild())
             .animon(childAnimon.getAnimon())
             .build();
  }
}
