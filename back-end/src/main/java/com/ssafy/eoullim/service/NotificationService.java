package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Notification;
import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.model.entity.NotificationEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

public interface NotificationService {
    void save(User user, String body);
    List<Notification> getNotifications(Authentication authentication);

    void deleteNotifications(Authentication authentication);
}
