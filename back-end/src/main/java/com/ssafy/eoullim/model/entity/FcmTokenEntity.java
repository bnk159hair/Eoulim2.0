package com.ssafy.eoullim.model.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Entity
@Table(
    name = "fcm_token",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_userid_token",
          columnNames = {"user_id", "token"}),
      @UniqueConstraint(
          name = "unique_childid_token",
          columnNames = {"child_id", "token"}),
    })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmTokenEntity extends BaseEntity {
  @Id
  @Column(name = "fcm_token_id", columnDefinition = "INT UNSIGNED")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "child_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private ChildEntity child;

  @Column(nullable = false)
  private String token;

  @Builder
  public FcmTokenEntity(Long id, UserEntity user, ChildEntity child, String token) {
    this.id = id;
    this.user = user;
    this.child = child;
    this.token = token;
  }
}
