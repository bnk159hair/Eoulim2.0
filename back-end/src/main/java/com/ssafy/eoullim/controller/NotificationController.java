package com.ssafy.eoullim.controller;

import com.ssafy.eoullim.dto.response.SuccessResponse;
import com.ssafy.eoullim.model.Notification;
import com.ssafy.eoullim.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse<List<Notification>> getNotifications(Authentication authentication) {
    List<Notification> notifications = notificationService.getNotifications(authentication);
    return new SuccessResponse<>(notifications);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse<Void> deleteNotifications(Authentication authentication) {
    notificationService.deleteNotifications(authentication);
    return new SuccessResponse<>(null);
  }
}
