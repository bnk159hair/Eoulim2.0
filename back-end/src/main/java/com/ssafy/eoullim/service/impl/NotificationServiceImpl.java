package com.ssafy.eoullim.service.impl;

import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.model.entity.NotificationEntity;
import com.ssafy.eoullim.model.entity.UserEntity;
import com.ssafy.eoullim.repository.jpa.NotificationRepository;
import com.ssafy.eoullim.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
  private final NotificationRepository notificationRepository;

  @Override
  @Transactional
  public void save(User user, String text) {
    notificationRepository.save(
        NotificationEntity.builder().user(UserEntity.of(user)).text(text).build());
  }
}
