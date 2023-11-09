package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.User;
import org.springframework.stereotype.Service;

public interface NotificationService {
    void save(User user, String body);
}
