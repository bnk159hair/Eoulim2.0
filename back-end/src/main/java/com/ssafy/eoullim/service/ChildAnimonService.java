package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Animon;
import com.ssafy.eoullim.model.Child;

import java.util.List;

public interface ChildAnimonService {
    void saveChildAnimon(Child child, List<Animon> animons);
}
