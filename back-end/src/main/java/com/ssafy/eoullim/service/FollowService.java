package com.ssafy.eoullim.service;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.Follow;
import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.model.entity.ChildEntity;
import com.ssafy.eoullim.model.entity.FollowEntity;
import com.ssafy.eoullim.repository.jpa.ChildRepository;
import com.ssafy.eoullim.repository.jpa.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
  //  private final ChildService childService;  // 순환 참조 때문에 인터페이스 만들어야 함
  private final FollowRepository followRepository;
  private final ChildRepository childRepository;

  @Transactional
  public void create(Long childId, Long friendId, User user) {
    // childId와 friendId가 같은 경우
    if (childId.equals(friendId)) throw new IllegalArgumentException("id가 같은 child끼리는 친구 할 수 없음.");

    // 순환 참조 해결 해야 함
    //    ChildEntity childEntity = ChildEntity.of(childService.getChild(childId, user.getId()));
    //    ChildEntity followingChildEntity = childService.getChildEntity(friendId);

    // 나
    ChildEntity childEntity =
        childRepository
            .findById(childId)
            .orElseThrow(() -> new EoullimApplicationException(ErrorCode.CHILD_NOT_FOUND));
    if (!childEntity.getUser().getId().equals(user.getId()))
      throw new EoullimApplicationException(ErrorCode.FORBIDDEN_NO_PERMISSION);
    // 친구
    ChildEntity followingChildEntity =
        childRepository
            .findById(friendId)
            .orElseThrow(() -> new EoullimApplicationException(ErrorCode.CHILD_NOT_FOUND));

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

  public List<Follow> getFollowsByChild(Child child) {
    List<FollowEntity> followEntities = followRepository.findByChild(ChildEntity.of(child));
    return followEntities.stream().map(Follow::fromEntity).collect(Collectors.toList());
  }
}
