package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.OtherChild;
import com.ssafy.eoullim.model.User;

import java.util.List;
import java.util.Optional;

public interface FcmTokenService {
    void saveFcmTokenOfChild(Child child, String token);

    void deleteFcmTokenOfChild(Child child, String token);

    void saveFcmTokenOfParent(User user, String token);

    void deleteFcmTokenOfParent(User user, String token);

    List<String> getFcmTokenOfFriend(Long friendId);
}
