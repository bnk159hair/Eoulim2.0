package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Child;

import java.io.IOException;

public interface FirebaseMessagingService {
    void invite(String targetToken, String sessionId, Child child, Child friend) throws IOException;
}
