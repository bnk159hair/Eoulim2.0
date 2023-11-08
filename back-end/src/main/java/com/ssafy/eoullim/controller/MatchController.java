package com.ssafy.eoullim.controller;

import com.ssafy.eoullim.dto.request.MatchFriendRequest;
import com.ssafy.eoullim.dto.request.MatchStartRequest;
import com.ssafy.eoullim.dto.request.MatchStopRequest;
import com.ssafy.eoullim.dto.response.SuccessResponse;
import com.ssafy.eoullim.model.Match;
import com.ssafy.eoullim.service.MatchService;
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
    List<Integer> guideSeq = matchStopRequest.getGuideScript();
    List<String> timeline = matchStopRequest.getTimeline();
    log.info("Random Stop Called " + sessionId);

    try {
      Recording recording = matchService.stopRandom(sessionId, guideSeq, timeline);
      return new SuccessResponse<>(recording);
    } catch (OpenViduJavaClientException | OpenViduHttpException | IOException | ParseException e) {
      throw new RuntimeException(e);
    }
  }

  /* 친구 만나기 */
  @PostMapping("/friend/start")
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse<?> startFrined(
      @Valid @RequestBody MatchFriendRequest matchFriendRequest, Authentication authentication) {
    String existSessionId = matchFriendRequest.getSessionId();
    Long childId = matchFriendRequest.getChildId();
    Long friendId = matchFriendRequest.getFriendId();

    Match result = matchService.startFriend(childId, friendId, existSessionId, authentication);

    return new SuccessResponse<>(result);
  }

  @PostMapping("/friend/stop")
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponse<?> stopFriend(@RequestBody Map<String, Object> params) {

    String sessionId = (String) params.get("sessionId");
    try {
      Recording recording = matchService.stopFriend(sessionId);
      return new SuccessResponse<>(recording);
    } catch (OpenViduJavaClientException | OpenViduHttpException | IOException | ParseException e) {
      log.info(e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
