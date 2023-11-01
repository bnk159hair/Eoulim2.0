package com.ssafy.eoullim.controller;

import com.ssafy.eoullim.dto.request.UserJoinRequest;
import com.ssafy.eoullim.dto.request.UserLoginRequest;
import com.ssafy.eoullim.dto.request.UserModifyRequest;
import com.ssafy.eoullim.dto.request.UserPwCheckRequest;
import com.ssafy.eoullim.dto.response.SuccessResponse;
import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.service.UserService;
import com.ssafy.eoullim.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UserController {

  private final UserService userService;

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    private SuccessResponse<User> join(@Valid @RequestBody UserJoinRequest request) {
        final var user = userService.join(
                request.getUsername(),
                request.getPassword(),
                request.getName(),
                request.getPhoneNumber()
        );
        return new SuccessResponse<>(HttpStatus.CREATED, user);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SuccessResponse<?> login(@Valid @RequestBody UserLoginRequest request) {
        String accessToken = userService.login(request.getUsername(), request.getPassword());
        return new SuccessResponse<>(accessToken);
    }

    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public SuccessResponse<?> logout(Authentication authentication) {
        userService.logout(authentication.getName());
        return new SuccessResponse<>(HttpStatus.NO_CONTENT,"로그아웃 성공!");
    }

    @GetMapping("/check-username/{username}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SuccessResponse<?> isAvailableUsername(@PathVariable @NotBlank String username) {
        final var isAvailable = userService.isAvailableUsername(username);
        return new SuccessResponse<>(isAvailable);
    }

    @PostMapping("/check-password")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SuccessResponse<?> isCorrectPassword(@Valid @RequestBody UserPwCheckRequest request, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        final var isCorrect = userService.isCorrectPassword(request.getPassword(), user.getPassword());
        return new SuccessResponse<>(isCorrect);
    }

    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public SuccessResponse<?> updatePassword(
            @Valid @RequestBody UserModifyRequest request, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        userService.updatePw(user, request.getCurPassword(), request.getNewPassword());
        return new SuccessResponse<>("password changed");
    }
}