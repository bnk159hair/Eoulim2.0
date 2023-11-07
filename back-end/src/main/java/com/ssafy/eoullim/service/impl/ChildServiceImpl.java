package com.ssafy.eoullim.service.impl;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.*;
import com.ssafy.eoullim.model.entity.*;
import com.ssafy.eoullim.repository.ChildCacheRepository;
import com.ssafy.eoullim.repository.jpa.*;
import com.ssafy.eoullim.service.AnimonService;
import com.ssafy.eoullim.service.ChildAnimonService;
import com.ssafy.eoullim.service.ChildService;
import com.ssafy.eoullim.service.FcmTokenService;
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
    private final ChildRepository childRepository;
    private final ChildAnimonRepository childAnimonRepository;
    private final ChildCacheRepository childCacheRepository;
    private final FcmTokenService fcmTokenService;
    private final FollowRepository followRepository;

    private Child getChildById(Long childId) {
        ChildEntity childEntity = childRepository
                .findById(childId)
                .orElseThrow(() -> new EoullimApplicationException(ErrorCode.CHILD_NOT_FOUND));
        return Child.fromEntity(childEntity);
    }

    private Child checkPermission(Long childId, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        if (user == null)
            throw new EoullimApplicationException(ErrorCode.AUTHENTICATION_NOT_FOUND);

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
        if (user == null)
            throw new EoullimApplicationException(ErrorCode.AUTHENTICATION_NOT_FOUND);

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
        if (user == null)
            throw new EoullimApplicationException(ErrorCode.AUTHENTICATION_NOT_FOUND);

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

    /**
     * Child - Animon 관련
     */
    public List<Animon> getAnimonList(Long childId, Authentication authentication) {
        Child child = checkPermission(childId, authentication);
        // ChildAnimon Table에서 Child가 가진 Animon 가져오기
        List<ChildAnimonEntity> childAnimonEntities =
                childAnimonRepository
                        .findByChildId(child.getId())
                        .orElseThrow(
                                () -> new EoullimApplicationException(
                                        ErrorCode.CHILD_ANIMON_NOT_FOUND, "사용자가 소유한 애니몬이 없습니다."));
        // ChildAnimon Entity -> Child Animon DTO
        List<ChildAnimon> childAnimons =
                childAnimonEntities.stream().map(ChildAnimon::fromEntity).collect(Collectors.toList());
        // Child Animon DTO -> Animon DTO
        return childAnimons.stream().map(ChildAnimon::getAnimon).collect(Collectors.toList());
    }

    @Transactional
    public Animon setProfileAnimon(Long childId, Long animonId) {
        ChildAnimonEntity childAnimonEntity =
                childAnimonRepository
                        .findByChildIdAndAnimonId(childId, animonId)
                        .orElseThrow(
                                () -> new EoullimApplicationException(
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
    public List<Child> getFriends(Long childId, Authentication authentication) {
        Child child = checkPermission(childId, authentication);
        // Follow DB에서 childId가 following 하는 child list 조회
        List<FollowEntity> followEntities = followRepository.findByChild(ChildEntity.of(child));
        List<Follow> follows =
                followEntities.stream().map(Follow::fromEntity).collect(Collectors.toList());
        return follows.stream().map(Follow::getFollowingChild).collect(Collectors.toList());
    }

    public OtherChild getOtherChild(Long participantId) {
        return OtherChild.fromChild(getChildById(participantId));
    }
}
