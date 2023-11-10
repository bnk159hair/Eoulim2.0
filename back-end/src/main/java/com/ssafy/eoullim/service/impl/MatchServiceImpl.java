package com.ssafy.eoullim.service.impl;

import com.ssafy.eoullim.dto.request.MatchStartRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.eoullim.exception.EoullimApplicationException;
import com.ssafy.eoullim.exception.ErrorCode;
import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.FcmMessage;
import com.ssafy.eoullim.model.Match;
import com.ssafy.eoullim.model.MatchWait;
import com.ssafy.eoullim.model.Room;
import com.ssafy.eoullim.service.*;
import com.ssafy.eoullim.utils.RandomGeneratorUtils;
import io.openvidu.java.client.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchServiceImpl implements MatchService {

  private final RecordService recordService;
  //  private final AlarmService alarmservice;
  private final ChildService childService;
  private final SimpMessageSendingOperations simpMessageSendingOperations;

  private final FirebaseMessagingService firebaseMessagingService;
  private final FcmTokenService fcmTokenService;
  private final NotificationService notificationService;
  private final ObjectMapper objectMapper;

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

  private final Queue<MatchWait> matchList = new LinkedList<>();

  @PostConstruct
  public void init() {
    this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
  }

  @Override
  public void startRandom_01(MatchStartRequest matchStartRequest){
    // Child ID가 현재 User의 Child가 맞는지 체크
    matchList.add(MatchWait.builder().childId(matchStartRequest.getChildId()).grade(matchStartRequest.getGrade()).priority((short)0).build());
  }

  @Scheduled(fixedDelay = 10000)
  public void makeMatch(){
    int initLen = matchList.size(); // 현재 큐에 있는 값만 빼기 위해서 - 함수 실행 중 큐에 들어오는 애들은 다음 스케쥴 때 진행할거여서 상관없음
    List<MatchWait> curList = new ArrayList<>(); // 큐에서 뺀 애들 저장하기 위해
    List<MatchWait> leftList = new ArrayList<>();

    boolean [] visited = new boolean[initLen];
    Arrays.fill(visited, false);
    int []cntNum = new int[4];
    int startIdx = 0;

    for(int i = 0; i < initLen; i++){
      curList.add(matchList.poll()); // 큐에서는 현재 개수만큼 빼고 새로 옮기기
    }
    Collections.sort(curList); // 학년 -> 우선순위, 둘다 내림차순 정렬
    for (MatchWait match : curList){
      cntNum[(int)match.getGrade()]++; // 학년 개수 count
    }

    for(int i=1; i< 4; i++){
      if (cntNum[i] < 2){ // 학년 인원이 2인 아래면 넘기기
        startIdx += cntNum[i];
        continue;
      }
      for(int j=0; j < cntNum[i]; j++){ // 짝수 인원만큼 매칭
        if(j == cntNum[j] - 1){
          MatchWait left = curList.get(startIdx+j);
          short newPriority = (short)(left.getGrade()+1);
          left.setPriority(newPriority);
          leftList.add(left);
          continue;
        }
        Long childOne = curList.get(startIdx+j).getChildId();
        Long childTwo = curList.get(startIdx+j+1).getChildId();
        j += 1;
        sendMatchResult(childOne, childTwo);
      }

      startIdx += cntNum[i];
    }
    //------------------------------------------------------------//
//    for(int i =0; i< visited.length ; i++){
//      if(visited[i])continue;
//      MatchWait left = curList.get(i);
//      short newPriority = (short)(left.getGrade()+1);
//      left.setPriority(newPriority);
//      leftList.add(left);
//    }
    leftList.sort(new Comparator<MatchWait>() { // 우선순위로 정렬
      @Override
      public int compare(MatchWait o1, MatchWait o2) {
        return o2.getPriority()- o1.getPriority();
      }
    });
    
    for(int i = 0; i < leftList.size(); i++){
      if(i == leftList.size()-1){
        matchList.add(leftList.get(i));
        continue;
      }
      MatchWait left = leftList.get(i);
      if(left.getPriority()>5){
        sendMatchResult(left.getChildId(), leftList.get(i+1).getChildId());
        i += 1; // 두 개 읽었으므로 인덱스 하나 건너뜀
      }
      else{
        matchList.add(left);
      }
    }
  }


  public void sendMatchResult(Long childOne, Long childTwo){
    // session ID 짓기
    String sessionId = buildSessionId(childOne);
    // Session Id를 통해 Open Vidu Session 생성
    Session session = createNewOpenViduSession(sessionId);
    mapSessions.put(sessionId, session);
    // Connection Token 생성
    String tokenOne = creatConnectionToken(session);
    String tokenTwo = creatConnectionToken(session);

    Room newRoom = newMakeNewRoom(sessionId, true, childOne, childTwo);
    mapRooms.put(sessionId, newRoom);

    simpMessageSendingOperations.convertAndSend("/topic/random/start"+childOne, new Match(sessionId, tokenOne, newRoom.getRandom()));
    simpMessageSendingOperations.convertAndSend("/topic/random/start"+childTwo, new Match(sessionId, tokenTwo, newRoom.getRandom()));

  }



  //--------------------------------------------------------------------------------------//


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
    if (isRandom) matchingQueue.add(newRoom);
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
    RecordingProperties recordingProperties =
            new RecordingProperties.Builder() // 녹화 설정
                    .outputMode(Recording.OutputMode.INDIVIDUAL)
                    .resolution("1280x960")
                    .frameRate(30)
                    .name("VideoInfo")
                    .build();

    SessionProperties sessionProperties =
        new SessionProperties.Builder()
            .customSessionId(sessionId)
                .recordingMode(RecordingMode.MANUAL)
                .defaultRecordingProperties(recordingProperties)
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

  private Room newMakeNewRoom(String sessionId, Boolean isRandom, Long childOneId, Long childTwoId) {
    Room newRoom = new Room();
    if (isRandom) {
      List<Integer> random = RandomGeneratorUtils.generateRandomNumbers(2, 12, 4);
      log.info("[EMPTY] randomNum is created - sessionId : " + sessionId);
      newRoom.setRandom(random); // random 일 때만
    }
    newRoom.setSessionId(sessionId);
    newRoom.setChildOne(childOneId); // 첫 입장자 아이디 저장
    newRoom.setChildTwo(childTwoId);
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

  private FcmMessage createFcmMessage(String token, String title, String body) {
    return FcmMessage.builder()
        .message(
            FcmMessage.Message.builder()
                .token(token)
                .notification(
                    FcmMessage.Notification.builder().title(title).body(body).image(null).build())
                .build())
        .validate_only(false)
        .build();
  }

  private String createInvitationMessageForFriend(
      String targetToken, String sessionId, String childName) throws JsonProcessingException {
    String title = childName + " 님이 보낸 초대장이 도착했습니다.";
    String body = "세션 ID: " + sessionId;
    return objectMapper.writeValueAsString(createFcmMessage(targetToken, title, body));
  }

  private String createInvitationMessageForParent(
      String targetToken, String childName, String friendName) throws JsonProcessingException {
    String title = friendName + " 님이 초대를 받았습니다.";
    String body = friendName + " 님이 " + childName + " 님께 초대를 받았습니다.";
    return objectMapper.writeValueAsString(createFcmMessage(targetToken, title, body));
  }

  private String createText(String childName, String friendName) {
    return friendName + " 님이 " + childName + " 님께 초대를 받았습니다.";
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
      Long childId, Long friendId, String existSessionId, Authentication authentication) {
    // child, friend
    final var child = childService.getChild(childId, authentication);
    final var friend = childService.getChildWithNoPermission(friendId);

    // 초대할 때
    if (existSessionId == null) {
      // 없는거 확인했으면 세로운 세션 Id 만들기
      Match result = createNewMatch(child.getId(), false);

      // sse 알림 서비스
      //      Alarm alarm = new Alarm(result.getSessionId(), child.getName());
      //      alarmservice.send(friendId, alarm);

      // fcm push 서비스
      Set<String> friendTokenSet = fcmTokenService.getFcmTokenOfFriend(friendId);
      Set<String> parentTokenSet = fcmTokenService.getFcmTokenOfParent(friend.getUser().getId());
      // 부모님 토큰과 아이 토큰이 겹치는 경우 빼주기 (아이가 부모님 폰을 사용하는 경우)
      parentTokenSet.removeAll(friendTokenSet);

      // 친구가 오프라인 인 경우
      if (friendTokenSet.isEmpty())
        throw new EoullimApplicationException(ErrorCode.FCM_TOKEN_NOT_FOUND, "친구가 오프라인입니다.");

      // 친구에게 초대 알림 보내기
      for (String targetToken : friendTokenSet) {
        try {
          log.info("friendToken " + targetToken);
          String message =
              createInvitationMessageForFriend(targetToken, result.getSessionId(), child.getName());
          firebaseMessagingService.invite(message);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }

      // 친구 부모님께 알림 DB에 저장하고 보내기
      notificationService.save(friend.getUser(), createText(child.getName(), friend.getName()));

      for (String targetToken : parentTokenSet) {
        try {
          log.info("parentToken " + targetToken);
          String message =
              createInvitationMessageForParent(targetToken, child.getName(), friend.getName());
          firebaseMessagingService.invite(message);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }

      return result;
    }
    // 초대 받았을 때
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
  public Recording stopRandom(String sessionId, List<Integer> guideSeq, List<String> timeline)
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
