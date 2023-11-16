package com.ssafy.eoullim.service.impl;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.Animon;
import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.ChildAnimon;
import com.ssafy.eoullim.repository.jpa.AnimonRepository;
import com.ssafy.eoullim.repository.jpa.ChildAnimonRepository;
import com.ssafy.eoullim.service.AnimonService;
import com.ssafy.eoullim.service.ChildAnimonService;
import com.ssafy.eoullim.service.ChildService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnimonServiceImpl implements AnimonService {
  // service
  private final ChildService childService;
  private final ChildAnimonService childAnimonService;
  // repos
  private final ChildAnimonRepository childAnimonRepository;

  @Override
  public Animon receiveAnimon(Long childId, Long otherChildId, Authentication authentication) {
    // 나
    Child child = childService.getChild(childId, authentication);
    // 상대방의 프로필 애니몬
    Child otherChild = childService.getChildWithNoPermission(otherChildId);
    Animon gift = otherChild.getProfileAnimon();
    log.info("[INFO] Friend's Profile Animon: "+gift.getId());
    // 나에게 상대방의 애니몬이 있는지 체크
    checkChildAnimon(childId, gift.getId());
    // 애니몬 저장
    ChildAnimon result = childAnimonService.saveChildAnimon(child, gift);
    log.info(String.format("[SUCCESS] Animon received. Animon name: %s", result.getAnimon().getName()));
    return result.getAnimon();
  }

  private void checkChildAnimon(Long childId, Long animonId) {
    // 이미 존재하는 경우
    childAnimonRepository.findByChildIdAndAnimonId(childId, animonId)
            .ifPresent((it) -> {throw new EoullimApplicationException(ErrorCode.CHILD_ANIMON_DUPLICATED);});
  }
}
