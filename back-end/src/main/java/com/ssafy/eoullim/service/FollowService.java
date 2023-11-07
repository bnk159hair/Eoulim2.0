package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.Follow;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface FollowService {
    void create(Long childId, Long friendId, Authentication authentication);

    List<Follow> getFollowList(Child child);
}
