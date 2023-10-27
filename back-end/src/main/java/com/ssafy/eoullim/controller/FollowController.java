package com.ssafy.eoullim.controller;

import com.ssafy.eoullim.dto.request.FollowRequest;
import com.ssafy.eoullim.dto.response.Response;
import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.service.FollowService;
import com.ssafy.eoullim.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/friendship")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping
    public Response<Void> create(@RequestBody FollowRequest request) {
        followService.create(request.getMyId(), request.getFriendId());
        return Response.success();
    }

    @GetMapping("/{childId}")
    public Response<List<Child>> getFriendsList(@PathVariable @NotBlank  Integer childId, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        List<Child> friendList = followService.getFriends(childId, user.getId());
        return Response.success(friendList);
    }

}
