package com.ssafy.eoullim.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.ssafy.eoullim.repository.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FcmTokenService {

    private final FcmTokenRepository fcmTokenRepository;

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "serviceAccount-File.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
