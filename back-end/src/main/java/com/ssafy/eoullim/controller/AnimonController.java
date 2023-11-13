package com.ssafy.eoullim.controller;

import com.ssafy.eoullim.dto.request.AnimonPresentRequest;
import com.ssafy.eoullim.dto.response.SuccessResponse;
import com.ssafy.eoullim.service.AnimonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("api/v2/animons")
@RequiredArgsConstructor
public class AnimonController {

  private final AnimonService animonService;

  /**
   * 화상 미팅이 끝난 후, 마지막에 내 프로필 애니몬을 상대방에게 선물 할 경우
   * @param request : childId - 주는 사람, otherChildId - 받는 사람
   * @return 결과
   */
  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse<?> presentAnimon(@Valid @RequestBody AnimonPresentRequest request, Authentication authentication) {
    animonService.prensentAnimon(request.getChildId(), request.getOtherChildId(), authentication);
    return new SuccessResponse<>(null);
  }
}
