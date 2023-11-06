package com.ssafy.eoullim.service.impl;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.Animon;
import com.ssafy.eoullim.repository.jpa.AnimonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnimonService {
  private final AnimonRepository animonRepository;

  public List<Animon> getAnimons() {
    return animonRepository.findAll().stream().map(Animon::fromEntity).collect(Collectors.toList());
  }

  public List<Animon> getAnimonsAtRandom(int limit) {
    return animonRepository
        .findRandomAnimals(limit)
        .orElseThrow(() -> new EoullimApplicationException(ErrorCode.DB_NOT_FOUND, "애니몬 없다. "))
        .stream()
        .map(Animon::fromEntity)
        .collect(Collectors.toList());
  }

  public Animon getMinIdAnimon(List<Animon> animons) {
    return animons.stream()
        .min(Comparator.comparing(Animon::getId))
        .orElseThrow(() -> new EoullimApplicationException(ErrorCode.DB_NOT_FOUND, "애니몬 없다. "));
  }
}
