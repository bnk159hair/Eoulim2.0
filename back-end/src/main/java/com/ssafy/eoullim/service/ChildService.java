package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Animon;
import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.OtherChild;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ChildService {
    Child login(Long childId, String fcmToken, Authentication authentication);

    void logout(Long childId, String token, Authentication authentication);

    Child create(Child child, Authentication authentication);

    List<Child> getChildren(Authentication authentication);

    Child getChild(Long childId, Authentication authentication);

    Child modify(Long childId, Child changedChild, Authentication authentication);

    void delete(Long childId, Authentication authentication);

    List<Animon> getAnimonList(Long childId, Authentication authentication);

    Animon setProfileAnimon(Long childId, Long animonId);

    List<Child> getFriends(Long childId, Authentication authentication);

    OtherChild getOtherChild(Long participantId);
}
