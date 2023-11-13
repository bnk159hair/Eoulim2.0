package com.ssafy.eoullim.service.impl;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.Notification;
import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.model.entity.NotificationEntity;
import com.ssafy.eoullim.model.entity.UserEntity;
import com.ssafy.eoullim.repository.jpa.NotificationRepository;
import com.ssafy.eoullim.service.NotificationService;
import com.ssafy.eoullim.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {
  private final NotificationRepository notificationRepository;

  @Override
  public void save(User user, String text) {
    notificationRepository.save(
        NotificationEntity.builder().user(UserEntity.of(user)).text(text).build());
  }

  @Override
  public List<Notification> getNotifications(Authentication authentication) {
    User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
    if (user == null) throw new EoullimApplicationException(ErrorCode.AUTHENTICATION_NOT_FOUND);
    return notificationRepository.findAllByUserId(user.getId()).stream()
        .map(Notification::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void deleteNotifications(Authentication authentication) {
    User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
    if (user == null) throw new EoullimApplicationException(ErrorCode.AUTHENTICATION_NOT_FOUND);
    notificationRepository.deleteByUserId(user.getId());
  }
}
