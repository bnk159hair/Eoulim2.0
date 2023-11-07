package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.User;

public interface FollowService {
    void create(Long childId, Long friendId, User user);
}
