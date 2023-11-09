package com.ssafy.eoullim.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.FcmMessage;
import com.ssafy.eoullim.service.FirebaseMessagingService;
import com.ssafy.eoullim.service.NotificationService;
import okhttp3.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {

  private final NotificationService notificationService;
  private final String API_URL =
      "https://fcm.googleapis.com/v1/projects/eoullim-7e5fb/messages:send";
  private final ObjectMapper objectMapper;

  public void invite(String targetToken, String sessionId, Child child, Child friend) throws IOException {
    String message = makeMessage(targetToken, sessionId, child, friend);

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

  private String makeMessage(String targetToken, String sessionId, Child child, Child friend) throws JsonProcessingException {
    String title, body;
    if (sessionId == null) {
      title = friend.getName() + " 님이 초대를 받았습니다.";
      body = friend.getName() + " 님이 " + child.getName() + " 님께 초대를 받았습니다.";
      notificationService.save(friend.getUser(), body);
    } else {
      title = child.getName() + " 님이 보낸 초대장이 도착했습니다.";
      body = "세션 ID: " + sessionId;
    }
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
