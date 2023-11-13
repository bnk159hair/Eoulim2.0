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
  public void prensentAnimon(Long childId, Long otherChildId, Authentication authentication) {
    // 나의 프로필 애니몬 가져오기
    Child child = childService.getChild(childId, authentication);
    Animon gift = child.getProfileAnimon();
    log.info("[INFO] My Profile Animon: "+gift.getId());
    // 상대방에게 내 애니몬이 있는지 체크
    checkChildAnimon(otherChildId, gift.getId());
    // 애니몬 저장
    Child otherChild = childService.getChildWithNoPermission(otherChildId);
    childAnimonService.saveChildAnimon(otherChild, gift);
  }

  private void checkChildAnimon(Long childId, Long animonId) {
    // 이미 존재하는 경우
    childAnimonRepository.findByChildIdAndAnimonId(childId, animonId)
            .ifPresent((it) -> {throw new EoullimApplicationException(ErrorCode.CHILD_ANIMON_DUPLICATED);});
  }
}
