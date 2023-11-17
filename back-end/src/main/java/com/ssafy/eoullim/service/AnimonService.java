package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Animon;
import org.springframework.security.core.Authentication;


public interface AnimonService {

    Animon receiveAnimon(Long childId, Long otherChildId, Authentication authentication);
}
