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
   * 화상 미팅이 끝난 후, 상대방을 친구 추가하면 그 사람의 애니몬을 얻을 수 있다.
   * @param request : childId - 받는 사람, otherChildId - 주는 사람 (새로 친구로 추가한 사람)
   * @return 결과
   */
  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse<?> presentAnimon(@Valid @RequestBody AnimonPresentRequest request, Authentication authentication) {
    final var receivedAnimon = animonService.receiveAnimon(request.getChildId(), request.getOtherChildId(), authentication);
    return new SuccessResponse<>(receivedAnimon);
  }
}