package com.ssafy.eoullim.service.impl;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.*;
import com.ssafy.eoullim.model.entity.*;
import com.ssafy.eoullim.repository.ChildCacheRepository;
import com.ssafy.eoullim.repository.jpa.*;
import com.ssafy.eoullim.service.AnimonService;
import com.ssafy.eoullim.service.ChildAnimonService;
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
  private final ChildCacheRepository childCacheRepository;
  private final FcmTokenRepository fcmTokenRepository;
  private final FollowRepository followRepository;

  @Transactional
  public Child login(Long childId, String token, Long userId) {
    final var child = getChildWithUser(childId, userId);
    final var childEntity = ChildEntity.of(child);
    childCacheRepository.setStatus(childEntity.getId());
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
    final var child = getChildWithUser(childId, userId);
    childCacheRepository.delete(child.getId());
    FcmTokenEntity fcmToken =
        fcmTokenRepository
            .findByChildId(child.getId())
            .orElseThrow(() -> new EoullimApplicationException(ErrorCode.FCM_TOKEN_NOT_FOUND));
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
    log.info(newChildEntity.getId().toString());
    // Child에게 애니몬 지급
    childAnimonService.saveChildAnimon(Child.fromEntity(newChildEntity), randomAnimons);

    return Child.fromEntity(newChildEntity);
  }

  @Transactional
  public Child modify(Long childId, Child changedChild, Long userId) {
    Child child = getChildWithUser(childId, userId);
    ChildEntity childEntity = ChildEntity.of(child);
    childEntity.modify(changedChild);
    return Child.fromEntity(childEntity);
  }

  @Transactional
  public void delete(Long childId, Long userId) {
    final var childEntity =
        childRepository
            .findByIdAndUserId(childId, userId)
            .orElseThrow(() -> new EoullimApplicationException(ErrorCode.FORBIDDEN_NO_PERMISSION));
    log.info(childEntity.getId()+" "+childEntity.getUser().getId());
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

  public Child getChildWithUser(Long childId, Long userId) {
    final var childEntity = getChildEntity(childId); // Child Entity 조회
    // 접근 권한이 있는 Child인지 체크
    if (!childEntity.getUser().getId().equals(userId))
      throw new EoullimApplicationException(ErrorCode.FORBIDDEN_NO_PERMISSION);
    return Child.fromEntity(childEntity);
  }

  public ChildEntity getChildEntity(Long childId) {
    return childRepository
        .findById(childId)
        .orElseThrow(() -> new EoullimApplicationException(ErrorCode.CHILD_NOT_FOUND));
  }

  /**
   * Child - Animon 관련
   */
  public List<Animon> getAnimonList(Long childId, Long userId) {
    Child child = getChildWithUser(childId, userId);
    // ChildAnimon Table에서 Child가 가진 Animon 가져오기
    List<ChildAnimonEntity> childAnimonEntities =
        childAnimonRepository
            .findByChildId(child.getId())
            .orElseThrow(
                () ->
                    new EoullimApplicationException(
                        ErrorCode.CHILD_ANIMON_NOT_FOUND, "사용자가 소유한 애니몬이 없습니다."));
    // ChildAnimon Entity -> Child Animon DTO
    List<ChildAnimon> childAnimons =
        childAnimonEntities.stream().map(ChildAnimon::fromEntity).collect(Collectors.toList());
    // Child Animon DTO -> Animon DTO
    return childAnimons.stream().map(ChildAnimon::getAnimon).collect(Collectors.toList());
  }

  @Transactional
  public Animon setProfileAnimon(Long childId, Long animonId) {
    //    !! Service가 아닌 Repository를 의존한 경우 !!
    //    장점: jpaEntity <-> model 변환을 하지 않아도 된다.
    // Child가 소유한 Animon임을 확인 한 후 해당 관계 DTO 가져오기
    ChildAnimonEntity childAnimonEntity =
        childAnimonRepository
            .findByChildIdAndAnimonId(childId, animonId)
            .orElseThrow(
                () ->
                    new EoullimApplicationException(
                        ErrorCode.CHILD_ANIMON_NOT_FOUND, "사용자가 소유한 애니몬이 아닙니다."));
    ChildEntity childEntity = childAnimonEntity.getChild();
    AnimonEntity animonEntity = childAnimonEntity.getAnimon();
    // 변경하려는 Animon을 Child의 프로필 Animon으로 지정.
    childEntity.setProfileAnimon(animonEntity);
    return Animon.fromEntity(animonEntity);
  }

  /**
   * Child - Follow 관련
   * Following하는 친구들 불러오기
    */
  public List<Child> getFriends(Long childId, User user) {
    Child child = getChildWithUser(childId, user.getId());

    // Follow DB에서 childId가 following 하는 child list 조회
    List<FollowEntity> followEntities = followRepository.findByChild(ChildEntity.of(child));
    List<Follow> follows =
        followEntities.stream().map(Follow::fromEntity).collect(Collectors.toList());
    return follows.stream().map(Follow::getFollowingChild).collect(Collectors.toList());
  }

  public OtherChild getOtherChild(Long participantId) {
    return OtherChild.fromEntity(getChildEntity(participantId));
  }
}
