package com.ssafy.eoullim.service.impl;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.Alarm;
import com.ssafy.eoullim.model.Match;
import com.ssafy.eoullim.model.Room;
import com.ssafy.eoullim.service.*;
import com.ssafy.eoullim.utils.RandomGeneratorUtils;
import io.openvidu.java.client.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchServiceImpl implements MatchService {
  @Getter
  private final RecordService recordService;
  private final AlarmService alarmservice;
  private final ChildService childService;
  private final FirebaseMessagingService firebaseMessagingService;
  private final FcmTokenService fcmTokenService;

  @Value("${OPENVIDU_URL}")
  private String OPENVIDU_URL;

  @Value("${OPENVIDU_SECRET}")
  private String OPENVIDU_SECRET;

  private OpenVidu openvidu;

  private Queue<Room> matchingQueue = new LinkedList<>();

  // Collection to pair session names and OpenVidu Session objects
  private Map<String, Session> mapSessions = new ConcurrentHashMap<>();

  // Collection to pair session names and tokens (the inner Map pairs tokens and role associated)
  private Map<String, String> sessionRecordings = new ConcurrentHashMap<>();
  private Map<String, Room> mapRooms = new ConcurrentHashMap<>();

  @PostConstruct
  public void init() {
    this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
  }

  private Match createNewMatch(Long childId, Boolean isRandom) {
    // session ID 짓기
    String sessionId = buildSessionId(childId);
    // Session Id를 통해 Open Vidu Session 생성
    Session session = createNewOpenViduSession(sessionId);
    mapSessions.put(sessionId, session);
    // Connection Token 생성
    String token = creatConnectionToken(session);
    // Room 생성
    Room newRoom = makeNewRoom(sessionId, isRandom, childId);
    mapRooms.put(sessionId, newRoom);
    // Random 매치이면 매칭 큐에 넣기
    if(isRandom) matchingQueue.add(newRoom);  
    // 랜덤 미팅이면 가이드 순서 넣기
    return isRandom
        ? new Match(sessionId, token, newRoom.getRandom())
        : new Match(sessionId, token, null);
  }

  private String buildSessionId(Long childId) {
    String formatNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    String sessionId = String.format("%d_%s", childId, formatNow);
    log.info("[EMPTY] SessionId is created: " + sessionId);
    if (this.mapSessions.get(sessionId) != null) { // 만들려는 세션 Id가 이미 존재 하는 지
      throw new EoullimApplicationException(ErrorCode.MATCH_CONFLICT);
    }
    return sessionId;
  }

  private Session createNewOpenViduSession(String sessionId) {
    SessionProperties sessionProperties =
        new SessionProperties.Builder()
            .customSessionId(sessionId)
            .recordingMode(RecordingMode.MANUAL)
            .build();
    try {
        return openvidu.createSession(sessionProperties);
    } catch (OpenViduJavaClientException | OpenViduHttpException e) {
      throw new EoullimApplicationException(ErrorCode.OPENVIDU_SERVER_ERROR, "Session 생성 실패");
    }
  }

  private String creatConnectionToken(Session session) {
    // 비어있는 설정을 통한 설정파일 지정
    ConnectionProperties connectionProperties =
        ConnectionProperties.fromJson(new HashMap<String, Object>()).build();
    try {
      String token = session.createConnection(connectionProperties).getToken();
      log.info("[EMPTY] Token is created - sessionId : " + session.getSessionId());
      return token;
    } catch (OpenViduJavaClientException | OpenViduHttpException e) {
      throw new EoullimApplicationException(ErrorCode.OPENVIDU_HTTP_ERROR, "Connection 생성 실패");
    }
  }

  private Room makeNewRoom(String sessionId, Boolean isRandom, Long childId) {
    Room newRoom = new Room();
    if (isRandom) {
      List<Integer> random = RandomGeneratorUtils.generateRandomNumbers(2, 12, 4);
      log.info("[EMPTY] randomNum is created - sessionId : " + sessionId);
      newRoom.setRandom(random); // random 일 때만
    }
    newRoom.setSessionId(sessionId);
    newRoom.setChildOne(childId); // 첫 입장자 아이디 저장
    return newRoom;
  }

  private String startRecording(String sessionId) { // 녹화
    RecordingProperties recordingProperties =
        new RecordingProperties.Builder() // 녹화 설정
            .outputMode(Recording.OutputMode.INDIVIDUAL)
            .resolution("1280x960")
            .frameRate(30)
            .name("VideoInfo")
            .build();
    try {
      Recording recording = openvidu.startRecording(sessionId, recordingProperties); // 녹화 시작
      sessionRecordings.put(sessionId, recording.getId());
      return recording.getId();
    } catch (OpenViduHttpException | OpenViduJavaClientException e) {
      throw new RuntimeException(e);
    }
  }

  private Session getExistingSession(String sessionId) {
    Session existingSession = mapSessions.get(sessionId);
    if (existingSession == null) {
      throw new EoullimApplicationException(ErrorCode.MATCH_NOT_FOUND);
    }
    log.info("[ALREADY] Session is already created: " + sessionId);
    return existingSession;
  }

  @Override
  @Transactional
  public synchronized Match startRandom(Long childId, Authentication authentication) {
    // Child ID가 현재 User의 Child가 맞는지 체크
    final var child = childService.getChild(childId, authentication);

    // 매칭 큐 - Empty
    if (matchingQueue.isEmpty()) {
      Match result = createNewMatch(child.getId(), true);
      return result;
    }
    // 매칭 큐 - Not Empty
    else { 
      Room existingRoom = matchingQueue.poll();
      String sessionId = existingRoom.getSessionId();

      Session existingSession = getExistingSession(sessionId);
      String token = creatConnectionToken(existingSession);

      Match result = new Match(sessionId, token, existingRoom.getRandom());

      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      existingRoom.setChildTwo(child.getId()); // 두번째 입장자 아이디 저장

      // 녹화 시작
      String recordingId = startRecording(sessionId);

      existingRoom.setRecordingId(recordingId);

      return result;
    }
  }

  @Override
  @Transactional
  public synchronized Match startFriend(
      Long childId,
      Long friendId,
      String existSessionId,
      Authentication authentication) {
    // Child ID가 현재 User의 Child가 맞는지 체크
    final var child = childService.getChild(childId, authentication);

    // 존재 하는 방이 없을 때
    if (existSessionId == null) {
      // 없는거 확인했으면 세로운 세션 Id 만들기
      Match result = createNewMatch(child.getId(), false);

      // 알림 서비스
//      Alarm alarm = new Alarm(result.getSessionId(), child.getName());
//      alarmservice.send(friendId, alarm);

      // push 서비스
      List<String> targetTokenList = fcmTokenService.getFcmTokenOfFriend(friendId);
      if (targetTokenList == null)
        throw new EoullimApplicationException(ErrorCode.FCM_TOKEN_NOT_FOUND, "친구가 오프라인입니다.");
      for (String targetToken : targetTokenList) {
        try {
          firebaseMessagingService.sendMessageTo(targetToken, result.getSessionId(), child.getName());
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      return result;
    }
    // existSessionId is not null
    else {
      String sessionId = existSessionId;
      Room existingRoom = mapRooms.get(sessionId);

      Session existingSession = getExistingSession(sessionId);
      String token = creatConnectionToken(existingSession);

      Match result = new Match(sessionId, token, null);

      String recordingId = startRecording(sessionId);

      existingRoom.setRecordingId(recordingId);
      existingRoom.setChildTwo(child.getId()); // 두번째 입장자 아이디 저장

      return result;
    }
  }

  @Override
  @Transactional
  public Recording stopRandom(
      String sessionId, List<Integer> guideSeq, List<String> timeline)
      throws OpenViduJavaClientException, OpenViduHttpException, IOException, ParseException {

    if (mapSessions.get(sessionId) != null && mapRooms.get(sessionId) != null) {

      Session session = mapSessions.get(sessionId);
      if (matchingQueue.contains(mapRooms.get(sessionId))) { // 매치가 안되었는데 나갔을 경우

        matchingQueue.remove(mapRooms.get(sessionId));
        mapSessions.remove(sessionId);
        mapRooms.remove(sessionId);

        throw new EoullimApplicationException(ErrorCode.MATCH_NOT_FOUND);

      } else { // 매치가 된 후 나갔을 경우
        String recordId = sessionRecordings.get(sessionId);
        Recording recording = openvidu.stopRecording(recordId);

        sessionRecordings.remove(sessionId);
        mapSessions.remove(sessionId);

        mapRooms.get(sessionId).setGuideSeq(guideSeq);

        mapRooms.get(sessionId).setTimeline(timeline);

        recordService.writeVideoToDB(recordId, mapRooms.get(sessionId));

        mapRooms.remove(sessionId);
        session.close();
        return recording;
      }
    } else {
      throw new EoullimApplicationException(ErrorCode.MATCH_NOT_FOUND);
    }
  }

  @Override
  @Transactional
  public Recording stopFriend(String sessionId)
      throws OpenViduJavaClientException, OpenViduHttpException, IOException, ParseException {
    log.info("sessionId: " + sessionId);
    if (mapSessions.get(sessionId) != null && mapRooms.get(sessionId) != null) {
      Session session = mapSessions.get(sessionId);
      if (matchingQueue.contains(mapRooms.get(sessionId))) { // 매치가 안되었는데 나갔을 경우

        mapSessions.remove(sessionId);
        mapRooms.remove(sessionId);

        throw new EoullimApplicationException(ErrorCode.MATCH_NOT_FOUND);

      } else { // 매치가 된 후 나갔을 경우
        String recordId = sessionRecordings.get(sessionId);
        Recording recording = openvidu.stopRecording(recordId);

        sessionRecordings.remove(sessionId);
        mapSessions.remove(sessionId);

        recordService.writeVideoToDB(recordId, mapRooms.get(sessionId));

        mapRooms.remove(sessionId);
        session.close();
        return recording;
      }
    } else {
      throw new EoullimApplicationException(ErrorCode.MATCH_CONFLICT);
    }
  }
}
