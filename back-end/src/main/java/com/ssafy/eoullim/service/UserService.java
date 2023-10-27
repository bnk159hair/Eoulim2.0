package com.ssafy.eoullim.service;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.model.UserRole;
import com.ssafy.eoullim.model.entity.UserEntity;
import com.ssafy.eoullim.repository.UserCacheRepository;
import com.ssafy.eoullim.repository.UserRepository;
import com.ssafy.eoullim.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserCacheRepository userCacheRepository;
    private final RedisTemplate<String, Object> blackListTemplate;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userCacheRepository.getUser(username).orElseGet(
                () -> userRepository.findByUsername(username).map(User::fromEntity).orElseThrow(
                        () -> new EoullimApplicationException(ErrorCode.USER_NOT_FOUND)));
    }

    public void join(String username, String password, String name, String phoneNumber) {
        userRepository.findByUsername(username).ifPresent(it -> {
            throw new EoullimApplicationException(ErrorCode.DUPLICATED_NAME);
        });
        userRepository.save(
                UserEntity.builder()
                        .username(username)
                        .password(encoder.encode(password))
                        .name(name)
                        .phoneNumber(phoneNumber)
                        .role(UserRole.USER)
                        .build()
        );
    }

    public String login(String username, String password) {
        User savedUser = loadUserByUsername(username);

        String key = setBlackListKey(username); // BlackList Key
        blackListTemplate.delete(key); // BlackList에서 삭제
        userCacheRepository.setUser(savedUser); // UserCache에 저장

        if (!encoder.matches(password, savedUser.getPassword())) {
            throw new EoullimApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        return JwtTokenUtils.generateAccessToken(username, secretKey, expiredTimeMs);
    }

    public void logout(String username) {
        String key = setBlackListKey(username);
        userCacheRepository.delete(username); // UserCache에서 삭제
        blackListTemplate.opsForValue().set(key,"logout", expiredTimeMs, TimeUnit.MILLISECONDS);
    }

    public String setBlackListKey(String username) {
        return "BlackList:" + username;
    }

    public boolean checkId(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean checkPw(String pwRequest, String pwCorrect) {
        return encoder.matches(pwRequest, pwCorrect);
    }

}
