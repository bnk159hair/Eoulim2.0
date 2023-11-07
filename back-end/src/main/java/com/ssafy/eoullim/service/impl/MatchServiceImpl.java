package com.ssafy.eoullim.service.impl;

import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.Alarm;
import com.ssafy.eoullim.model.Match;
import com.ssafy.eoullim.model.Room;
import com.ssafy.eoullim.service.MatchService;
import com.ssafy.eoullim.service.RecordService;
import com.ssafy.eoullim.utils.RandomGeneratorUtils;
import io.openvidu.java.client.*;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class MatchServiceImpl implements MatchService {
  @Value("${OPENVIDU_URL}")
  private String OPENVIDU_URL;

  @Value("${OPENVIDU_SECRET}")
  private String OPENVIDU_SECRET;

  private OpenVidu openvidu;

  private Queue<Room> matchingQueue = new LinkedList<Room>();
  // Collection to pair session names and OpenVidu Session objects
  private Map<String, Session> mapSessions = new ConcurrentHashMap<>();
  // Collection to pair session names and tokens (the inner Map pairs tokens and
  // role associated)

  private Map<String, String> sessionRecordings = new ConcurrentHashMap<>();
  private Map<String, Room> mapRooms = new ConcurrentHashMap<>();

  @PostConstruct
  public void init() {
    this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
  }

  @Override
  public synchronized Match startRandom(Long childId)
      throws OpenViduJavaClientException, OpenViduHttpException, InterruptedException {
//    Map<String, Object> params = new HashMap<>(); // 빈 파일
//    ConnectionProperties connectionProperties = ConnectionProperties.fromJson(params).build();
    /* 비어있는 설정을 통한 설정파일 지정 */
    ConnectionProperties connectionProperties = ConnectionProperties.fromJson(new HashMap<String, Object>()).build();


//    LocalDateTime now = LocalDateTime.now();
    String formatNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    String sessionId = childId.toString() + "_" + formatNow; // sessionId 결정

    if (this.mapSessions.get(sessionId) != null) { // 만드려는 세션 Id가 이미 존재하는지
      throw new EoullimApplicationException(ErrorCode.MATCH_CONFLICT);
    }
    if (matchingQueue.isEmpty()) { // 비어있다면
      Room newRoom = new Room();
      matchingQueue.add(newRoom);

      log.info("[EMPTY] SessionId created: " + sessionId);

      SessionProperties sessionProperties =
          new SessionProperties.Builder()
              .customSessionId(sessionId)
              .recordingMode(RecordingMode.MANUAL)
              .build();

      Session session = openvidu.createSession(sessionProperties);
      log.info("[EMPTY] SessionId is created: " + sessionId);

      String token = session.createConnection(connectionProperties).getToken();
      log.info("[EMPTY] Token is created - sessionId : " + sessionId);

      List<Integer> random = RandomGeneratorUtils.generateRandomNumbers(2, 12, 4);
      log.info("[EMPTY] randomNum is created - sessionId : " + sessionId);

      newRoom.setSessionId(session.getSessionId());
      newRoom.setRandom(random);

      mapSessions.put(sessionId, session);

      newRoom.setChildOne(childId); // 첫 입장자 아이디 저장
      mapRooms.put(sessionId, newRoom);
      Match result = new Match(sessionId, token, random);

      return result;
    } else { // 비어있지 않다면
      Room existingRoom = matchingQueue.poll();
      sessionId = existingRoom.getSessionId();
      if (mapSessions.get(sessionId) != null) { // 세션이 정상적으로 존재한다면
        log.info("[ALREADY] Session created: " + sessionId);
        String token = mapSessions.get(sessionId).createConnection(connectionProperties).getToken();

        Match result = new Match(sessionId, token, existingRoom.getRandom());
        RecordingProperties recordingProperties =
            new RecordingProperties.Builder() // 녹화 설정
                .outputMode(Recording.OutputMode.INDIVIDUAL)
                .resolution("1280x960")
                .frameRate(30)
                .name("VideoInfo")
                .build();
        Thread.sleep(3000);

        existingRoom.setChildTwo(childId); // 두번째 입장자 아이디 저장
        Recording recording = openvidu.startRecording(sessionId, recordingProperties); // 녹화 시작
        sessionRecordings.put(sessionId, recording.getId());
        existingRoom.setRecordingId(recording.getId());
        return result;
      } else {
        throw new EoullimApplicationException(ErrorCode.MATCH_NOT_FOUND);
      }
    }
  }

  @Override
  public Recording stopRandom(
      String sessionId, List<Integer> guideSeq, List<String> timeline, RecordService recordService)
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
  public synchronized Match startFriend(
      Long childId,
      String childName,
      Long friendId,
      String existSessionId,
      AlarmService alarmService)
      throws OpenViduJavaClientException, OpenViduHttpException {
//    Map<String, Object> params = new HashMap<>(); // 빈 파일
//    ConnectionProperties connectionProperties = ConnectionProperties.fromJson(params).build();

    ConnectionProperties connectionProperties = ConnectionProperties.fromJson(new HashMap<String, Object>()).build();

    if (existSessionId == null) { // 존재하는 방이 없을 때
//      LocalDateTime now = LocalDateTime.now();
      String formatNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
      String sessionId = childId.toString() + "_" + formatNow; // sessionId 결정

      if (this.mapSessions.get(sessionId) != null) { // 만드려는 세션 Id가 이미 존재하는지
        throw new EoullimApplicationException(ErrorCode.MATCH_CONFLICT);
      } else { // 없는거 확인했으면 세로운 세션 Id 만들기
        log.info("[EMPTY] Session created: " + sessionId);

        SessionProperties sessionProperties =
            new SessionProperties.Builder()
                .customSessionId(sessionId)
                .recordingMode(RecordingMode.MANUAL)
                .build();

        Session session = openvidu.createSession(sessionProperties);

        String token = session.createConnection(connectionProperties).getToken();

        Room newRoom = new Room();
        newRoom.setSessionId(session.getSessionId());

        mapSessions.put(sessionId, session);
        Match result = new Match(sessionId, token, null);
        newRoom.setChildOne(childId); // 첫 입장자 아이디 저장
        mapRooms.put(sessionId, newRoom);

        /*
        알림 서비스 코드 작성
         */
        Alarm alarm = new Alarm(sessionId, childName);
        alarmService.send(friendId, alarm);

        return result;
      }
    } else {
      if (mapSessions.get(existSessionId) != null) { // 방이 실제로 존재할 때
        String sessionId = existSessionId;
        if (mapSessions.get(sessionId) != null) { // 세션이 정상적으로 존재한다면
          Room existingRoom = mapRooms.get(sessionId);
          log.info("[ALREADY] Session created: " + sessionId);
          String token =
              mapSessions.get(sessionId).createConnection(connectionProperties).getToken();
          Match result = new Match(sessionId, token, null);

          RecordingProperties recordingProperties =
              new RecordingProperties.Builder() // 녹화 설정
                  .outputMode(Recording.OutputMode.INDIVIDUAL)
                  .resolution("1280x960")
                  .frameRate(30)
                  .name("VideoInfo")
                  .build();
          Recording recording = openvidu.startRecording(sessionId, recordingProperties); // 녹화 시작

          sessionRecordings.put(sessionId, recording.getId());
          existingRoom.setRecordingId(recording.getId());
          existingRoom.setChildTwo(childId); // 두번째 입장자 아이디 저장

          return result;
        } else {
          throw new EoullimApplicationException(ErrorCode.MATCH_NOT_FOUND);
        }
      } else {
        throw new EoullimApplicationException(ErrorCode.MATCH_NOT_FOUND);
      }
    }
  }

  @Override
  public Recording stopFriend(String sessionId, RecordService recordService)
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
