package com.ssafy.eoullim.controller;

import com.ssafy.eoullim.dto.request.MatchFriendRequest;
import com.ssafy.eoullim.dto.request.MatchStartRequest;
import com.ssafy.eoullim.dto.request.MatchStopRequest;
import com.ssafy.eoullim.dto.response.SuccessResponse;
import com.ssafy.eoullim.model.Match;
import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.service.MatchService;
import com.ssafy.eoullim.service.RecordService;
import com.ssafy.eoullim.service.impl.AlarmService;
import com.ssafy.eoullim.utils.ClassUtils;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.Recording;

import java.io.IOException;
import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/meetings")
@RequiredArgsConstructor
public class MatchController {
  private final RecordService recordService;
  private final AlarmService alarmService;
  private final MatchService matchService;

  @PostMapping("/random/start")
  @ResponseStatus(HttpStatus.OK)
  @Transactional
  public synchronized SuccessResponse<?> startRandom(
      @Valid @RequestBody MatchStartRequest request, Authentication authentication) {
    Match result = matchService.startRandom(request.getChildId(), authentication);
    return new SuccessResponse<>(result);
  }

  @PostMapping("/random/stop")
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse<?> stopRandom(@RequestBody MatchStopRequest matchStopRequest) {

    String sessionId = matchStopRequest.getSessionId();
    List<Integer> guideSeq = matchStopRequest.getGuideSeq();
    List<String> timeline = matchStopRequest.getTimeline();
    log.info("Random Stop Called " + sessionId);

    Recording recording = null;
    try {
      recording = matchService.stopRandom(sessionId, guideSeq, timeline, recordService);
    } catch (OpenViduJavaClientException | OpenViduHttpException | IOException | ParseException e) {
      throw new RuntimeException(e);
    }

    return new SuccessResponse<>(recording);
  }

  /* 친구 만나기 */
  @PostMapping("/friend/start")
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse<?> startFrined(
      @Valid @RequestBody MatchFriendRequest matchFriendRequest, Authentication authentication) {
    User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
    String existSessionId = matchFriendRequest.getSessionId();
    Long childId = matchFriendRequest.getChildId();
    String childName = matchFriendRequest.getName();
    Long friendId = matchFriendRequest.getFriendId();

    Match result =
        matchService.startFriend(
            childId, childName, friendId, alarmService, existSessionId, authentication);

    return new SuccessResponse<>(result);
  }

  @PostMapping("/friend/stop")
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse<?> stopFriend(@RequestBody Map<String, Object> params) {

    String sessionId = (String) params.get("sessionId");
    Recording recording = null;
    try {
      recording = matchService.stopFriend(sessionId, recordService);
    } catch (OpenViduJavaClientException | OpenViduHttpException | IOException | ParseException e) {
      log.info(e.getMessage());
      throw new RuntimeException(e);
    }
    return new SuccessResponse<>(recording);
  }
}
