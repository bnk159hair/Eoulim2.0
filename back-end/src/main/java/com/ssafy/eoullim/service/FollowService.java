package com.ssafy.eoullim.service;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.model.entity.ChildEntity;
import com.ssafy.eoullim.model.entity.FollowEntity;
import com.ssafy.eoullim.repository.jpa.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {
  // Service
  private final ChildService childService;
  // Repo
  private final FollowRepository followRepository;

  @Transactional
  public void create(Long childId, Long friendId, User user) {
    // childId와 friendId가 같은 경우
    if (childId.equals(friendId)) throw new IllegalArgumentException("id가 같은 child끼리는 친구 할 수 없음.");

    // 나와 상대방 Child Entity 조회
    ChildEntity childEntity = ChildEntity.of(childService.getChildWithUser(childId, user.getId()));
    ChildEntity followingChildEntity = childService.getChildEntity(friendId);

    // 이미 좋아요 누른 친구인 경우
    followRepository
        .findByChildAndFollowingChild(childEntity, followingChildEntity)
        .ifPresent(
            it -> {
              throw new EoullimApplicationException(ErrorCode.DUPLICATED_FRIEND);
            });
    // follow 등록
    followRepository.save(FollowEntity.of(childEntity, followingChildEntity));
  }
}
