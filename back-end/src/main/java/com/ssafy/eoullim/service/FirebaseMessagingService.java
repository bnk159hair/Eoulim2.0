package com.ssafy.eoullim.service;

import java.io.IOException;

public interface FirebaseMessagingService {
    void sendMessageTo(String targetToken, String sessionId, String childName) throws IOException;
}
