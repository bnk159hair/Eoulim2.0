package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Child;

import java.io.IOException;

public interface FirebaseMessagingService {
    void send(String message) throws IOException;
}
