package com.ssafy.eoullim.model.entity;

import javax.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Table(name = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationEntity extends BaseEntity{
  @Id
  @Column(name = "notification_id", columnDefinition = "INT UNSIGNED")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private UserEntity user;

  @Column(nullable = false)
  private String text;

  @Builder
  public NotificationEntity(Long id, UserEntity user, String text) {
    this.id = id;
    this.user = user;
    this.text = text;
  }
}
