package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Match;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.Recording;
import org.json.simple.parser.ParseException;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;

public interface MatchService {
  Match startRandom(Long childId, Authentication authentication);

  Match startFriend(
          Long childId,
          Long friendId,
          String existSessionId,
          Authentication authentication);

  Recording stopRandom(
      String sessionId, List<Integer> guideSeq, List<String> timeline)
      throws OpenViduJavaClientException, OpenViduHttpException, IOException, ParseException;

  Recording stopFriend(String sessionId)
      throws OpenViduJavaClientException, OpenViduHttpException, IOException, ParseException;
}
