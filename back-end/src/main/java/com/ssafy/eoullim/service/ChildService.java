package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Animon;
import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.OtherChild;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ChildService {
    // Login, Logout
    Child login(Long childId, String fcmToken, Authentication authentication);
    void logout(Long childId, String token, Authentication authentication);
    // Child CRUD
    Child create(Child child, Authentication authentication);
    List<Child> getChildren(Authentication authentication);
    Child getChild(Long childId, Authentication authentication);
    Child modify(Long childId, Child changedChild, Authentication authentication);
    void delete(Long childId, Authentication authentication);
    // Child - Animon
    List<Animon> getAnimonList(Long childId, Authentication authentication);
    Animon setProfileAnimon(Long childId, Long animonId, Authentication authentication);
    // Child - Follow
    List<OtherChild> getFriends(Long childId, Authentication authentication);
    Child getChildWithNoPermission(Long participantId);     // follow create에 필요
    OtherChild getOtherChild(Long participantId);
    


    }
