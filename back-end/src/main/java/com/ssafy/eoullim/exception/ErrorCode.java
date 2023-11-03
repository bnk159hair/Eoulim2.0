package com.ssafy.eoullim.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  // Common Error
  UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED, "A-001", "유효 하지 않은 토큰 입니다."),
  FORBIDDEN_NO_PERMISSION(HttpStatus.FORBIDDEN, "A-002", "금지된 접근 입니다."),
  DB_NOT_FOUND(HttpStatus.NOT_FOUND, "A-003", "DB에 없는 데이터."),
  // Server Error
  OPEN_API_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S-001", "Open API Connection error occurs"),
  NOTIFICATION_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S-002", "SSE notification Connection occurs error"),

  // FcmToken Error
  FCM_TOKEN_NOT_FOUNT(HttpStatus.NOT_FOUND, "FCM-001", "존재 하지 않는 FcmToken 입니다."),
  // Child Error
  CHILD_NOT_FOUND(HttpStatus.NOT_FOUND, "CH-001", "존재 하지 않는 Child 입니다."),
  CHILD_ANIMON_NOT_FOUND(HttpStatus.NOT_FOUND, "CH-002", "사용자가 소유한 애니몬을 찾을 수 없음."),
  // User Error
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U-001", "존재 하지 않는 User 입니다."),
  DUPLICATED_NAME(HttpStatus.BAD_REQUEST, "U-002", "이미 사용 중인 ID 입니다."),
  INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "U-003", "유효 하지 않은 비밀 번호 입니다."),
  // Friend Error
  DUPLICATED_FRIEND(HttpStatus.BAD_REQUEST, "F-001", "이미 등록된 친구 입니다."),
  // OpenVidu Error
  MATCH_NOT_FOUND(HttpStatus.NOT_FOUND, "OV-001", "Match not found. 해당 회의 방이 존재 하지 않습니다."),
  MATCH_CONFLICT(HttpStatus.CONFLICT, "OV-002", "Match Conflict. 이미 존재하는 회의 방입니다."),
  OPENVIDU_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "OV-003", "Openvidu server error occurs"),
  OPENVIDU_HTTP_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "OV-004", "Openvidu http error occurs");

  private final HttpStatus status;
  private final String code; // 네이버에서 쓰는 에러 코드. 찾기 쉬움
  private final String message; // 이건 Client에게 보여줄 message
}
