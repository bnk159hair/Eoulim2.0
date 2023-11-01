package com.ssafy.eoullim.controller;

import com.ssafy.eoullim.dto.request.UserJoinRequest;
import com.ssafy.eoullim.dto.request.UserLoginRequest;
import com.ssafy.eoullim.dto.request.UserModifyRequest;
import com.ssafy.eoullim.dto.request.UserPwCheckRequest;
import com.ssafy.eoullim.dto.response.Response;
import com.ssafy.eoullim.dto.response.SuccessResponse;
import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.service.UserService;
import com.ssafy.eoullim.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
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
    public ResponseEntity<SuccessResponse<?>> login(@Valid @RequestBody UserLoginRequest request) {
        String accessToken = userService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new SuccessResponse<>(accessToken));
    }

    @GetMapping("/logout")
    public Response<Void> logout(Authentication authentication) {
        userService.logout(authentication.getName());
        return Response.success(HttpStatus.OK, "logout completed");
    }

    @GetMapping("/check-username/{username}")
    public Response<String> checkUsername(@PathVariable @NotBlank String username) {
        boolean duplicate = userService.checkId(username);
        if(duplicate) {
            return Response.success(HttpStatus.OK, "duplicate ID", "duplicated");
        }
        else {
            return Response.success(HttpStatus.OK, "available ID", "available");
        }
    }

    @PostMapping("/check-password")
    public Response<String> checkPassword(@Valid @RequestBody UserPwCheckRequest request, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        boolean correct = userService.checkPw(request.getPassword(), user.getPassword());
        if(correct) {
            return Response.success(HttpStatus.OK, "correct password", "correct");
        }
        else {
            return Response.success(HttpStatus.OK, "wrong password", "wrong");
        }
    }

    @PatchMapping("/password")
    public Response<Void> updatePassword(
            @Valid @RequestBody UserModifyRequest request, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        userService.updatePw(user, request.getCurPassword(), request.getNewPassword());
        return Response.success(HttpStatus.OK, "password changed");
    }
}
