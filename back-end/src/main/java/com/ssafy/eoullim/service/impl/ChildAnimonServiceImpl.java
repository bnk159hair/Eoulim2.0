package com.ssafy.eoullim.service.impl;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.Animon;
import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.ChildAnimon;
import com.ssafy.eoullim.model.entity.ChildAnimonEntity;
import com.ssafy.eoullim.repository.jpa.ChildAnimonRepository;
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
public class ChildAnimonServiceImpl implements ChildAnimonService {

  private final ChildAnimonRepository childAnimonRepository;

  @Override
  @Transactional
  public ChildAnimon saveChildAnimon(Child child, Animon animon) {
    ChildAnimonEntity childAnimonEntity = ChildAnimonEntity.builder()
            .child(child).animon(animon).build();
    final var newChildAnimonEntity = childAnimonRepository.save(childAnimonEntity);
    return ChildAnimon.fromEntity(newChildAnimonEntity);
  }

  @Override
  @Transactional
  public void saveChildAnimons(Child child, List<Animon> animons) {
    List<ChildAnimonEntity> childAnimonEntities =
        animons.stream()
            .map(animon -> ChildAnimonEntity.builder().animon(animon).child(child).build())
            .collect(Collectors.toList());
    childAnimonRepository.saveAll(childAnimonEntities);
  }

  @Override
  public List<ChildAnimon> getChildAnimonList(Long childId) {
    List<ChildAnimonEntity> childAnimonEntities =
        childAnimonRepository
            .findAllByChildId(childId)
            .orElseThrow(
                () ->
                    new EoullimApplicationException(
                        ErrorCode.CHILD_ANIMON_NOT_FOUND, "사용자가 소유한 애니몬이 없습니다."));
    return childAnimonEntities.stream().map(ChildAnimon::fromEntity).collect(Collectors.toList());
  }

  @Override
  public ChildAnimon getChildAnimon(Long childId, Long animonId) {
    ChildAnimonEntity childAnimonEntity =
        childAnimonRepository
            .findByChildIdAndAnimonId(childId, animonId)
            .orElseThrow(
                () ->
                    new EoullimApplicationException(
                        ErrorCode.CHILD_ANIMON_NOT_FOUND, "사용자가 소유한 애니몬이 아닙니다."));
    return ChildAnimon.fromEntity(childAnimonEntity);
  }
}
