package com.ssafy.eoullim.service;

import com.ssafy.eoullim.model.Match;
import com.ssafy.eoullim.service.impl.AlarmService;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.Recording;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public interface MatchService {
  Match startRandom(Long childId) throws OpenViduJavaClientException, OpenViduHttpException, InterruptedException;

  Recording stopRandom(
      String sessionId, List<Integer> guideSeq, List<String> timeline, RecordService recordService) throws OpenViduJavaClientException, OpenViduHttpException, IOException, ParseException ;

  Match startFriend(
      Long childId,
      String childName,
      Long friendId,
      String existSessionId,
      AlarmService alarmService)  throws OpenViduJavaClientException, OpenViduHttpException;

  Recording stopFriend(String sessionId, RecordService recordService)
      throws OpenViduJavaClientException, OpenViduHttpException, IOException, ParseException;
}
