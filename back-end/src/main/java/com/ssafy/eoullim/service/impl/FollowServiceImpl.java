package com.ssafy.eoullim.service.impl;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.Follow;
import com.ssafy.eoullim.model.OtherChild;
import com.ssafy.eoullim.model.entity.ChildEntity;
import com.ssafy.eoullim.model.entity.FollowEntity;
import com.ssafy.eoullim.repository.jpa.FollowRepository;
import com.ssafy.eoullim.service.ChildService;
import com.ssafy.eoullim.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

  private final ChildService childService;
  private final FollowRepository followRepository;

  private Child getChild(Long childId, Authentication authentication) {
    return childService.getChild(childId, authentication);
  }

  private Child getFollowingChild(Long followingId) {
    return childService.getChildWithNoPermission(followingId);
  }

  @Override
  @Transactional
  public OtherChild create(Long childId, Long followingId, Authentication authentication) {
    // childId와 followingId 같은 경우 예외 처리
    if (childId.equals(followingId))
      throw new IllegalArgumentException("id가 같은 child끼리는 친구 할 수 없음.");

    ChildEntity childEntity = ChildEntity.of(getChild(childId, authentication));
    ChildEntity followingChildEntity = ChildEntity.of(getFollowingChild(followingId));
    // 이미 좋아요 누른 친구인 경우 예외 처리
    followRepository
        .findByChildAndFollowingChild(childEntity, followingChildEntity)
        .ifPresent(
            it -> {
              throw new EoullimApplicationException(ErrorCode.DUPLICATED_FRIEND);
            });
    // follow 등록
    FollowEntity followEntity = followRepository.save(FollowEntity.of(childEntity, followingChildEntity));
    return OtherChild.fromChild(Child.fromEntity(followEntity.getFollowingChild()));
  }

  @Override
  @Transactional
  public void delete(Long childId, Long followingChildId, Authentication authentication) {
    // childId와 followingId 같은 경우 예외 처리
    if (childId.equals(followingChildId))
      throw new IllegalArgumentException("id가 같은 child끼리는 친구 할 수 없음.");

    ChildEntity childEntity = ChildEntity.of(getChild(childId, authentication));
    ChildEntity followingChildEntity = ChildEntity.of(getFollowingChild(followingChildId));

    FollowEntity followEntity =
        followRepository
            .findByChildAndFollowingChild(childEntity, followingChildEntity)
            .orElseThrow(
                () -> new EoullimApplicationException(ErrorCode.FOLLOW_NOT_FOUND, "내 친구가 아니에요."));
    followRepository.delete(followEntity);
  }
}
