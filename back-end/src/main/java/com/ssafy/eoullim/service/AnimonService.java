package com.ssafy.eoullim.service;

import org.springframework.security.core.Authentication;


public interface AnimonService {

    void prensentAnimon(Long childId, Long otherChildId, Authentication authentication);
}
