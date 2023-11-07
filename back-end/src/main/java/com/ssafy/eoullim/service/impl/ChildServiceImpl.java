package com.ssafy.eoullim.service.impl;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.*;
import com.ssafy.eoullim.model.entity.*;
import com.ssafy.eoullim.repository.ChildCacheRepository;
import com.ssafy.eoullim.repository.jpa.*;
import com.ssafy.eoullim.service.*;
import com.ssafy.eoullim.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChildServiceImpl implements ChildService {

  private final AnimonService animonService;
  private final ChildAnimonService childAnimonService;
  private final FcmTokenService fcmTokenService;
  private final ChildRepository childRepository;
  private final FollowRepository followRepository;
  private final ChildCacheRepository childCacheRepository;

  private Child getChildById(Long childId) {
    ChildEntity childEntity =
        childRepository
            .findById(childId)
            .orElseThrow(() -> new EoullimApplicationException(ErrorCode.CHILD_NOT_FOUND));
    return Child.fromEntity(childEntity);
  }

  private Child checkPermission(Long childId, Authentication authentication) {
    User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
    if (user == null) throw new EoullimApplicationException(ErrorCode.AUTHENTICATION_NOT_FOUND);

    Child child = getChildById(childId);
    if (!child.getUser().getId().equals(user.getId()))
      throw new EoullimApplicationException(ErrorCode.FORBIDDEN_NO_PERMISSION);

    return child;
  }

  private void setOnline(Long childId) {
    childCacheRepository.setOnline(childId);
  }

  private void setOffline(Long childId) {
    childCacheRepository.setOffline(childId);
  }

  private void saveFcmToken(Child child, String token) {
    fcmTokenService.saveFcmTokenOfChild(child, token);
  }

  private void deleteFcmToken(Child child, String token) {
    fcmTokenService.deleteFcmTokenOfChild(child, token);
  }

  private List<Animon> getRandomAnimons(int count) {
    return animonService.getAnimonsAtRandom(count);
  }

  private AnimonEntity getProfileAnimon(List<Animon> animons) {
    return AnimonEntity.of(animonService.getMinIdAnimon(animons));
  }

  private void saveChildAnimon(ChildEntity childEntity, List<Animon> randomAnimons) {
    childAnimonService.saveChildAnimon(Child.fromEntity(childEntity), randomAnimons);
  }

  private List<ChildAnimon> getChildAnimonList(Long childId) {
    return childAnimonService.getChildAnimonList(childId);
  }

  private ChildAnimon getChildAnimon(Long childId, Long animonId) {
    return childAnimonService.getChildAnimon(childId, animonId);
  }

  private List<Follow> getFollowList(Child child) {
    List<FollowEntity> followEntities = followRepository.findAllByChild(ChildEntity.of(child));
    return followEntities.stream().map(Follow::fromEntity).collect(Collectors.toList());
  }

  @Transactional
  public Child login(Long childId, String fcmToken, Authentication authentication) {
    Child child = checkPermission(childId, authentication);
    setOnline(childId);
    saveFcmToken(child, fcmToken);
    return child;
  }

  @Transactional
  public void logout(Long childId, String token, Authentication authentication) {
    Child child = checkPermission(childId, authentication);
    setOffline(childId);
    deleteFcmToken(child, token);
  }

  @Transactional
  public Child create(Child child, Authentication authentication) {
    User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
    if (user == null) throw new EoullimApplicationException(ErrorCode.AUTHENTICATION_NOT_FOUND);

    // 저장할 ChildEntity 생성
    ChildEntity childEntity = ChildEntity.of(UserEntity.of(user), child);

    // 애니몬 랜덤으로 2개 가져오기
    List<Animon> randomAnimons = getRandomAnimons(2);
    // 가져온 애니몬 중에 가장 작은 ID 가진 애니몬을 기본 프로필 애니몬으로 선택
    AnimonEntity profileAnimon = getProfileAnimon(randomAnimons);
    childEntity.setProfileAnimon(profileAnimon);

    // child 저장
    ChildEntity savedChild = childRepository.save(childEntity);
    log.info(savedChild.getId().toString());
    // Child에게 애니몬 지급
    saveChildAnimon(savedChild, randomAnimons);
    return Child.fromEntity(savedChild);
  }

  @Transactional
  public List<Child> getChildren(Authentication authentication) {
    User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
    if (user == null) throw new EoullimApplicationException(ErrorCode.AUTHENTICATION_NOT_FOUND);

    return childRepository
        .findAllByUserId(user.getId())
        .orElseThrow(() -> new EoullimApplicationException(ErrorCode.USER_NOT_FOUND))
        .stream()
        .map(Child::fromEntity)
        .collect(Collectors.toList());
  }

  @Transactional
  public Child getChild(Long childId, Authentication authentication) {
    return checkPermission(childId, authentication);
  }

  @Transactional
  public Child modify(Long childId, Child changedChild, Authentication authentication) {
    Child child = checkPermission(childId, authentication);
    ChildEntity childEntity = ChildEntity.of(child);
    childEntity.updateInfo(changedChild);
    return Child.fromEntity(childEntity);
  }

  @Transactional
  public void delete(Long childId, Authentication authentication) {
    Child child = checkPermission(childId, authentication);
    ChildEntity childEntity = ChildEntity.of(child);
    childRepository.delete(childEntity);
  }

  /** Child - Animon 관련 */
  @Transactional
  public List<Animon> getAnimonList(Long childId, Authentication authentication) {
    checkPermission(childId, authentication);
    List<ChildAnimon> childAnimons = getChildAnimonList(childId);
    return childAnimons.stream().map(ChildAnimon::getAnimon).collect(Collectors.toList());
  }

  @Transactional
  public Animon setProfileAnimon(Long childId, Long animonId, Authentication authentication) {
    checkPermission(childId, authentication);
    ChildAnimonEntity childAnimonEntity = ChildAnimonEntity.of(getChildAnimon(childId, animonId));
    ChildEntity childEntity = childAnimonEntity.getChild();
    AnimonEntity animonEntity = childAnimonEntity.getAnimon();
    // 변경하려는 Animon을 Child의 프로필 Animon으로 지정.
    childEntity.setProfileAnimon(animonEntity);
    return Animon.fromEntity(animonEntity);
  }

  /** Child - Follow 관련 Following하는 친구들 불러오기 */
  @Transactional
  public List<Child> getFriends(Long childId, Authentication authentication) {
    Child child = checkPermission(childId, authentication);
    List<Follow> followList = getFollowList(child);
    return followList.stream().map(Follow::getFollowingChild).collect(Collectors.toList());
  }

  @Transactional
  public OtherChild getOtherChild(Long participantId) {
    return OtherChild.fromChild(getChildById(participantId));
  }
}
