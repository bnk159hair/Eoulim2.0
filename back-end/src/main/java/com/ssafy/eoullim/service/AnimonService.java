package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Animon;

import java.util.List;

public interface AnimonService {
    List<Animon> getAnimons();

    List<Animon> getAnimonsAtRandom(int limit);

    Animon getMinIdAnimon(List<Animon> animons);
}
