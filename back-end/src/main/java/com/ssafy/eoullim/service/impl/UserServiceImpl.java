package com.ssafy.eoullim.service.impl;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.model.UserRole;
import com.ssafy.eoullim.model.entity.UserEntity;
import com.ssafy.eoullim.repository.UserCacheRepository;
import com.ssafy.eoullim.repository.jpa.UserRepository;
import com.ssafy.eoullim.service.FcmTokenService;
import com.ssafy.eoullim.service.UserService;
import com.ssafy.eoullim.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final FcmTokenService fcmTokenService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserCacheRepository userCacheRepository;
    private final RedisTemplate<String, Object> blackListTemplate;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expired-time-ms}")
    private Long expiredTimeMs;

    private String setBlackListKey(String username) {
        return "BlackList:" + username;
    }

    private void saveFcmToken(User user, String token) {
        log.info("service method call");
        fcmTokenService.saveFcmTokenOfParent(user, token);
    }

    private void deleteFcmToken(User user, String token) {
        fcmTokenService.deleteFcmTokenOfParent(user, token);
    }

    @Transactional
    public User join(String username, String password, String name, String phoneNumber) {
        // Exception: User 중복 가입 방지
        userRepository.findByUsername(username).ifPresent(it -> {
            throw new EoullimApplicationException(ErrorCode.DUPLICATED_NAME);
        });
        // save new user
        UserEntity newUserEntity = userRepository.save(
                UserEntity.builder()
                        .username(username)
                        .password(encoder.encode(password))
                        .name(name)
                        .phoneNumber(phoneNumber)
                        .role(UserRole.USER)
                        .build()
        );
        // return new user saved in DB
        return User.fromEntity(newUserEntity);
    }

    @Transactional
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userCacheRepository.getUser(username).orElseGet(
                () -> userRepository.findByUsername(username).map(User::fromEntity).orElseThrow(
                        () -> new EoullimApplicationException(ErrorCode.USER_NOT_FOUND)));
    }

    @Transactional
    public String login(String username, String password, String fcmToken) {
        User savedUser = loadUserByUsername(username);
        if (!encoder.matches(password, savedUser.getPassword()))
            throw new EoullimApplicationException(ErrorCode.INVALID_PASSWORD);

        userCacheRepository.setUser(savedUser); // UserCache에 저장

        String key = setBlackListKey(username);
        blackListTemplate.delete(key); // BlackList에서 삭제

        log.info("api call");
        saveFcmToken(savedUser, fcmToken); // fcm token 저장
        return JwtTokenUtils.generateAccessToken(username, secretKey, expiredTimeMs);
    }

    @Transactional
    public void logout(String username, String fcmToken) {
        User user = userCacheRepository.getUser(username).orElseThrow(
                () -> new EoullimApplicationException(ErrorCode.ALREADY_LOGOUT));

        String key = setBlackListKey(username); // BlackList에 등록
        blackListTemplate.opsForValue().set(key,"logout", expiredTimeMs, TimeUnit.MILLISECONDS);

        userCacheRepository.delete(username); // UserCache에서 삭제

        deleteFcmToken(user, fcmToken); // fcm token 삭제
    }

    @Transactional
    public Boolean isAvailableUsername(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    @Transactional
    public Boolean isCorrectPassword(String pwRequest, String pwCorrect) {
        return encoder.matches(pwRequest, pwCorrect);
    }

    @Transactional
    public void updatePw(User user, String curPassword, String newPassword) {
        if (!encoder.matches(curPassword, user.getPassword())) {
            throw new EoullimApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(UserEntity.of(user));
        userCacheRepository.setUser(user);
    }
}
