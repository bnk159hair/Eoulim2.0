package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Animon;
import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.ChildAnimon;
import com.ssafy.eoullim.model.entity.AnimonEntity;
import com.ssafy.eoullim.model.entity.ChildAnimonEntity;
import com.ssafy.eoullim.model.entity.ChildEntity;
import com.ssafy.eoullim.repository.jpa.ChildAnimonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChildAnimonService {
  private final ChildAnimonRepository childAnimonRepository;

  @Transactional
  public void saveChildAnimon(Child child, List<Animon> animons) {
    List<ChildAnimonEntity> childAnimonEntities =
        animons.stream()
            .map(animon -> ChildAnimonEntity.builder().animon(animon).child(child).build())
            .collect(Collectors.toList());
    childAnimonRepository.saveAll(childAnimonEntities);
  }
}
