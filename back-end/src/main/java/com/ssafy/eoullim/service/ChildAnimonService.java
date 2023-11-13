package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Animon;
import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.ChildAnimon;

import java.util.List;

public interface ChildAnimonService {
    void saveChildAnimon(Child child, Animon animon);

    void saveChildAnimons(Child child, List<Animon> animons);

    List<ChildAnimon> getChildAnimonList(Long childId);

    ChildAnimon getChildAnimon(Long childId, Long animonId);
}
