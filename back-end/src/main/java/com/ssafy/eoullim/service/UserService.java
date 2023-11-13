package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.User;

public interface UserService {
    User join(String username, String password, String name, String phoneNumber);

    String login(String username, String password, String fcmToken);

    void logout(String name, String fcmToken);

    void updatePw(User user, String curPassword, String newPassword);

    Boolean isAvailableUsername(String username);

    Boolean isCorrectPassword(String password, String password1);

    User loadUserByUsername(String userName);
}
