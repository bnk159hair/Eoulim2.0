package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Animon;
import com.ssafy.eoullim.repository.AnimonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnimonService {
  AnimonRepository animonRepository;

  public List<Animon> getAnimons() {
    return animonRepository.findAll().stream().map(Animon::fromEntity).collect(Collectors.toList());
  }
}
