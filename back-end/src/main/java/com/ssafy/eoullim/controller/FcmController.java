package com.ssafy.eoullim.controller;

import com.ssafy.eoullim.dto.request.FcmRequest;
import com.ssafy.eoullim.service.FcmTokenService;
import com.ssafy.eoullim.service.FirebaseMessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FcmController {

    private final FirebaseMessagingService firebaseMessagingService;

    @PostMapping("/push")
    public ResponseEntity<?> pushMessage(@RequestBody FcmRequest fcmRequest) {
        System.out.println(fcmRequest.getTargetToken() + "\n"
                + fcmRequest.getTitle() + " " + fcmRequest.getBody());
        try {
            firebaseMessagingService.sendMessageTo(
                    fcmRequest.getTargetToken(),
                    fcmRequest.getTitle(),
                    fcmRequest.getBody());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }
}
