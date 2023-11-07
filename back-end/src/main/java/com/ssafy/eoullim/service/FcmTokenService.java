package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.User;

public interface FcmTokenService {
    void saveFcmTokenOfChild(Child child, String token);

    void deleteFcmTokenOfChild(Child child, String token);

    void saveFcmTokenOfParent(User user, String token);

    void deleteFcmTokenOfParent(User user, String token);
}
