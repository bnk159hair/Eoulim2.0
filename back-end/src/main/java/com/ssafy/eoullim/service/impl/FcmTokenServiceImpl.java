package com.ssafy.eoullim.service.impl;

import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.model.entity.ChildEntity;
import com.ssafy.eoullim.model.entity.FcmTokenEntity;
import com.ssafy.eoullim.model.entity.UserEntity;
import com.ssafy.eoullim.repository.jpa.FcmTokenRepository;
import com.ssafy.eoullim.service.FcmTokenService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmTokenServiceImpl implements FcmTokenService {

  private final FcmTokenRepository fcmTokenRepository;

  @Override
  @Transactional
  public void saveFcmTokenOfChild(Child child, String token) {
    if (fcmTokenRepository.findByChildIdAndToken(child.getId(), token) == null) {
      fcmTokenRepository.save(
          FcmTokenEntity.builder().child(ChildEntity.of(child)).token(token).build());
    }
  }

  @Override
  @Transactional
  public void deleteFcmTokenOfChild(Child child, String token) {
    FcmTokenEntity fcmToken = fcmTokenRepository.findByChildIdAndToken(child.getId(), token);
    if (fcmToken != null) fcmTokenRepository.delete(fcmToken);
  }

  @Override
  @Transactional
  public void saveFcmTokenOfParent(User user, String token) {
    if (fcmTokenRepository.findByUserIdAndToken(user.getId(), token) == null) {
      fcmTokenRepository.save(
              FcmTokenEntity.builder().user(UserEntity.of(user)).token(token).build());
    }
  }

  @Override
  @Transactional
  public void deleteFcmTokenOfParent(User user, String token) {
    FcmTokenEntity fcmToken = fcmTokenRepository.findByUserIdAndToken(user.getId(), token);
    if (fcmToken != null) fcmTokenRepository.delete(fcmToken);
  }

  @Override
  @Transactional
  public List<String> getFcmTokenOfFriend(Long friendId) {
    return fcmTokenRepository.findAllByChildId(friendId).stream()
        .map(FcmTokenEntity::getToken)
        .collect(Collectors.toList());
  }
}
