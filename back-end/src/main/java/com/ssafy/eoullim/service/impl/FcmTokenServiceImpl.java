package com.ssafy.eoullim.service.impl;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.OtherChild;
import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.model.entity.ChildEntity;
import com.ssafy.eoullim.model.entity.FcmTokenEntity;
import com.ssafy.eoullim.model.entity.UserEntity;
import com.ssafy.eoullim.repository.jpa.FcmTokenRepository;
import com.ssafy.eoullim.service.FcmTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmTokenServiceImpl implements FcmTokenService {

  private final FcmTokenRepository fcmTokenRepository;

  @Transactional
  public void saveFcmTokenOfChild(Child child, String token) {
    fcmTokenRepository.save(
        FcmTokenEntity.builder().child(ChildEntity.of(child)).token(token).build());
  }

  @Transactional
  public void deleteFcmTokenOfChild(Child child, String token) {
    FcmTokenEntity fcmToken =
        fcmTokenRepository
            .findByChildIdAndToken(child.getId(), token)
            .orElseThrow(() -> new EoullimApplicationException(ErrorCode.FCM_TOKEN_NOT_FOUND));
    fcmTokenRepository.delete(fcmToken);
  }

  @Transactional
  public void saveFcmTokenOfParent(User user, String token) {
    fcmTokenRepository.save(
        FcmTokenEntity.builder().user(UserEntity.of(user)).token(token).build());
  }

  @Transactional
  public void deleteFcmTokenOfParent(User user, String token) {
    FcmTokenEntity fcmToken =
        fcmTokenRepository
            .findByUserIdAndToken(user.getId(), token)
            .orElseThrow(() -> new EoullimApplicationException(ErrorCode.FCM_TOKEN_NOT_FOUND));
    fcmTokenRepository.delete(fcmToken);
  }

  @Override
  @Transactional
  public List<String> getFcmTokenOfFriend(Long friendId) {
    return fcmTokenRepository.findAllByChildId(friendId).stream()
        .map(FcmTokenEntity::getToken)
        .collect(Collectors.toList());
  }
}
