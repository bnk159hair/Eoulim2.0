package com.ssafy.eoullim.service.impl;

import com.ssafy.eoullim.model.Animon;
import com.ssafy.eoullim.model.Child;
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
  public void saveChildAnimon(Child child, List<Animon> animons) {
    List<ChildAnimonEntity> childAnimonEntities =
        animons.stream()
            .map(animon -> ChildAnimonEntity.builder().animon(animon).child(child).build())
            .collect(Collectors.toList());
    childAnimonRepository.saveAll(childAnimonEntities);
  }
}
