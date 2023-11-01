package com.ssafy.eoullim.controller;

import com.ssafy.eoullim.dto.request.FollowRequest;
import com.ssafy.eoullim.dto.response.SuccessResponse;
import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.service.FollowService;
import com.ssafy.eoullim.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/follows")
@RestController
public class FollowController {
    private final FollowService followService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public SuccessResponse<?> create(@Valid @RequestBody FollowRequest request) {
        followService.create(request.getChildId(), request.getFollowingChildId());
        return new SuccessResponse<>(HttpStatus.CREATED, null);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SuccessResponse<List<Child>> getFriendsList(@Valid @RequestBody FollowRequest request, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        List<Child> friendList = followService.getFriends(request.getChildId(), user.getId());
        return new SuccessResponse<>(friendList);
    }
}