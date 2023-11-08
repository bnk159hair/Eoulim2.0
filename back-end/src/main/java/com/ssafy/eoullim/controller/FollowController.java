package com.ssafy.eoullim.controller;

import com.ssafy.eoullim.dto.request.FollowRequest;
import com.ssafy.eoullim.dto.response.SuccessResponse;
import com.ssafy.eoullim.model.OtherChild;
import com.ssafy.eoullim.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/follows")
@RestController
public class FollowController {

  private final FollowService followService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public SuccessResponse<OtherChild> create(
      @Valid @RequestBody FollowRequest request, Authentication authentication) {
    OtherChild friend = followService.create(request.getChildId(), request.getFollowingChildId(), authentication);
    return new SuccessResponse<>(HttpStatus.CREATED, friend);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public SuccessResponse<?> delete(
      @Valid @RequestBody FollowRequest request, Authentication authentication) {
    followService.delete(request.getChildId(), request.getFollowingChildId(), authentication);
    return new SuccessResponse<>(HttpStatus.NO_CONTENT, null);
  }
}
