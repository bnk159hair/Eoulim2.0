package com.ssafy.eoullim.service;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.*;
import com.ssafy.eoullim.model.entity.*;
import com.ssafy.eoullim.repository.ChildCacheRepository;
import com.ssafy.eoullim.repository.jpa.ChildAnimonRepository;
import com.ssafy.eoullim.repository.jpa.FcmTokenRepository;
import com.ssafy.eoullim.repository.jpa.AnimonRepository;
import com.ssafy.eoullim.repository.jpa.ChildRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChildService {
  private final AnimonService animonService;
  private final ChildAnimonService childAnimonService;

  private final ChildRepository childRepository;
  private final ChildAnimonRepository childAnimonRepository;
  private final AnimonRepository animonRepository;
  private final ChildCacheRepository childCacheRepository;
  private final FcmTokenRepository fcmTokenRepository;

  @Transactional
  public Child login(Long childId, String token, Long userId) {
    final var childEntity = getChildEntity(childId);
    if (!childEntity.getUser().getId().equals(userId))
      throw new EoullimApplicationException(ErrorCode.FORBIDDEN_NO_PERMISSION);
    childCacheRepository.setStatus(childId);
    fcmTokenRepository.save(
        FcmTokenEntity.builder()
            .user(childEntity.getUser())
            .child(childEntity)
            .token(token)
            .build());
    return Child.fromEntity(childEntity);
  }

  @Transactional
  public void logout(Long childId, Long userId) {
    final var childEntity = getChildEntity(childId);
    if (!childEntity.getUser().getId().equals(userId))
      throw new EoullimApplicationException(ErrorCode.FORBIDDEN_NO_PERMISSION);
    childCacheRepository.delete(childId);
    FcmTokenEntity fcmToken =
        fcmTokenRepository
            .findByChildId(childId)
            .orElseThrow(() -> new EoullimApplicationException(ErrorCode.FCM_TOKEN_NOT_FOUNT));
    fcmTokenRepository.delete(fcmToken);
  }

  @Transactional
  public Child create(User user, Child child) {
    // 저장할 ChildEntity 생성
    ChildEntity childEntity = ChildEntity.of(UserEntity.of(user), child);

    // 애니몬 랜덤으로 2개 가져오기
    List<Animon> randomAnimons = animonService.getAnimonsAtRandom(2);
    // 가져온 애니몬 중에 가장 작은 ID 가진 애니몬을 기본 프로필 애니몬으로 선택
    AnimonEntity animonEntity = AnimonEntity.of(animonService.getMinIdAnimon(randomAnimons));
    childEntity.setProfileAnimon(animonEntity);

    // child 저장
    final var newChildEntity = childRepository.save(childEntity);
    log.error(newChildEntity.getId().toString());
    // Child에게 애니몬 지급
    childAnimonService.saveChildAnimon(Child.fromEntity(newChildEntity), randomAnimons);

    return Child.fromEntity(newChildEntity);
  }

  @Transactional
  public Child modify(Long childId, Child child) {
    final var childEntity = getChildEntity(childId);
    childEntity.modify(child);
    return Child.fromEntity(childEntity);
  }

  @Transactional
  public void delete(Long childId, Long userId) {
    final var childEntity =
        childRepository
            .findByIdAndUserId(childId, userId)
            .orElseThrow(() -> new EoullimApplicationException(ErrorCode.FORBIDDEN_NO_PERMISSION));
    childRepository.delete(childEntity);
  }

  public List<Child> getChildren(Long userId) {
    return childRepository
        .findAllByUserId(userId)
        .orElseThrow(() -> new EoullimApplicationException(ErrorCode.USER_NOT_FOUND))
        .stream()
        .map(Child::fromEntity)
        .collect(Collectors.toList());
  }

  public Child getChild(Long childId, Long userId) {
    final var childEntity = getChildEntity(childId); // 일단 실제 있는 Child인지 조회
    // 그 Child가 User의 Child인지
    if (!childEntity.getUser().getId().equals(userId))
      throw new EoullimApplicationException(ErrorCode.FORBIDDEN_NO_PERMISSION);
    return Child.fromEntity(childEntity);
  }

  public ChildEntity getChildEntity(Long childId) {
    return childRepository
        .findById(childId)
        .orElseThrow(() -> new EoullimApplicationException(ErrorCode.CHILD_NOT_FOUND));
  }

  public List<Animon> getAnimonList(Long childId) {
    final var childAnimons = childAnimonService.getChildAnimonsByChildId(childId);

    return childAnimons.stream().map(ChildAnimon::getAnimon).collect(Collectors.toList());
  }

  @Transactional
  public Animon setProfileAnimon(Long childId, Long animonId) {
    // Child가 소유한 Animon임을 확인 한 후 해당 관계 DTO 가져오기
    ChildAnimon childAnimon = childAnimonService.getChildAnimonByChildIdAndAnimonId(childId, animonId);
    // 변경하려는 Animon을 Child의 프로필 Animon으로 지정.
    AnimonEntity animonEntity = AnimonEntity.of(childAnimon.getAnimon());
    ChildEntity childEntity = ChildEntity.of(childAnimon.getChild());
    childEntity.setProfileAnimon(animonEntity);
    // 왜 나는 이거 안하면 저장 안되지?
    childRepository.save(childEntity);
    return Animon.fromEntity(animonEntity);

//    !! childAnimonRepository에 의존했을 경우 !!
//    장점: jpaEntity <-> model 변환을 하지 않아도 된다.
//          service간 순환참조 문제가 발생하지 않는다.
//          단일 책임 원칙을 지킬 수 있다.
//    ChildAnimonEntity childAnimonEntity = childAnimonRepository.findByChildIdAndAnimonId(childId, animonId)
//            .orElseThrow(() -> new EoullimApplicationException(ErrorCode.CHILD_ANIMON_NOT_FOUND, "사용자가 소유한 애니몬이 아닙니다."));
//    ChildEntity childEntity = childAnimonEntity.getChild();
//    AnimonEntity animonEntity = childAnimonEntity.getAnimon();
//    childEntity.setProfileAnimon(animonEntity);
//    return Animon.fromEntity(animonEntity);

  }

  public OtherChild getOtherChild(Long participantId) {
    return OtherChild.fromEntity(getChildEntity(participantId));
  }
}
