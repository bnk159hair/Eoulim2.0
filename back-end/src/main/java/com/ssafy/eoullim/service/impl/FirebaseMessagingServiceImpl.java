package com.ssafy.eoullim.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.ssafy.eoullim.model.FcmMessage;
import com.ssafy.eoullim.service.FirebaseMessagingService;
import okhttp3.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {

  private final String API_URL =
      "https://fcm.googleapis.com/v1/projects/eoullim-7e5fb/messages:send";
  private final ObjectMapper objectMapper;

  public void sendMessageTo(String targetToken, String sessionId, String childName) throws IOException {
    String message = makeMessage(targetToken, sessionId, childName);

    OkHttpClient client = new OkHttpClient();
    RequestBody requestBody =
        RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
    Request request =
        new Request.Builder()
            .url(API_URL)
            .post(requestBody)
            .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
            .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
            .build();

    Response response = client.newCall(request).execute();

    System.out.println(response.body().string());
  }

  private String makeMessage(String targetToken, String sessionId, String childName) throws JsonProcessingException {
    String title = childName + " 님이 보낸 초대장이 도착했습니다.";
    String body = "세션 ID: " + sessionId;
    FcmMessage fcmMessage =
        FcmMessage.builder()
            .message(
                FcmMessage.Message.builder()
                    .token(targetToken)
                    .notification(
                        FcmMessage.Notification.builder()
                            .title(title)
                            .body(body)
                            .image(null)
                            .build())
                    .build())
            .validate_only(false)
            .build();

    return objectMapper.writeValueAsString(fcmMessage);
  }

  private String getAccessToken() throws IOException {
    String firebaseConfigPath = "serviceAccount-File.json";

    GoogleCredentials googleCredentials =
        GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
            .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

    googleCredentials.refreshIfExpired();
    return googleCredentials.getAccessToken().getTokenValue();
  }
}
