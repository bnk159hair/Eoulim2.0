package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.OtherChild;
import org.springframework.security.core.Authentication;

public interface FollowService {
    OtherChild create(Long childId, Long followingChildId, Authentication authentication);

    void delete(Long childId, Long followingChildId, Authentication authentication);
}
