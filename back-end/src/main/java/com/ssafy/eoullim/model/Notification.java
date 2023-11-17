package com.ssafy.eoullim.model;

import com.ssafy.eoullim.model.entity.NotificationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Notification {
  private String text;
  private LocalDateTime createTime;

  public static Notification fromEntity(NotificationEntity entity) {
    return new Notification(entity.getText(), entity.getCreatedAt());
  }
}
