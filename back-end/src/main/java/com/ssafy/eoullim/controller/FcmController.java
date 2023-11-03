package com.ssafy.eoullim.controller;

import com.ssafy.eoullim.dto.request.FcmRequest;
import com.ssafy.eoullim.service.FirebaseMessagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FcmController {

    private final FirebaseMessagingService firebaseMessagingService;

    @PostMapping("/api/fcm")
    public ResponseEntity<?> pushMessage(@RequestBody FcmRequest fcmRequest) throws IOException {
        System.out.println(fcmRequest.getTargetToken() + " "
                + fcmRequest.getTitle() + " " + fcmRequest.getBody());

        firebaseMessagingService.sendMessageTo(
                fcmRequest.getTargetToken(),
                fcmRequest.getTitle(),
                fcmRequest.getBody());
        return ResponseEntity.ok().build();
    }

}
