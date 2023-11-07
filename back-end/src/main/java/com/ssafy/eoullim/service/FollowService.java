package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.User;
import org.springframework.security.core.Authentication;

public interface FollowService {
    void create(Long childId, Long friendId, Authentication authentication);
}
