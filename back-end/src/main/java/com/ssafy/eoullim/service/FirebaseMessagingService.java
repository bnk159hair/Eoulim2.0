package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Child;

import java.io.IOException;

public interface FirebaseMessagingService {
    void invite(String message) throws IOException;
}
