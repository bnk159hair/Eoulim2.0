package com.ssafy.eoullim.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequest {
  @NotBlank
  @Pattern(
      regexp = "[a-zA-Z0-9]{3,20}",
      message = "아이디는 영어 대소문자 혹은 숫자만 입력 가능합니다. (3~20자)") // 첫 글자는 영어 대소문자, 그 뒤는 영어 대소문자 혹은 숫자 3~20자
  String username;

  @NotBlank
  //    @Pattern(regexp = "[a-zA-Z0-9]{8,20}")
  //  @Pattern(
  //      regexp =
  // "^(?=(.*[0-9]){1,})(?=(.*[a-z]){1,})(?=(.*[A-Z]){1,})(?=(.*[@#$%^&+=]){0,}).{8,20}$",
  //      message =
  //          "문자열은 숫자([0-9]), 소문자([a-z]), 대문자([A-Z]), 특수 문자([@#$%^&+=]) 중 3가지 이상을 포함해야 합니다.\n"
  //              + "문자열은 총 길이가 8자 이상 20자 이하 이어야 합니다.")
  @Length(min = 3, max = 20)
  String password;

  @NotBlank
  @Pattern(regexp = "^[가-힣]{2,17}$", message = "이름은 17자 이하의 한글 문자여야 합니다.")
  String name;

  @NotBlank
//  @Pattern(regexp = "01[0-9]-[0-9]{4}-[0-9]{4}")
  @Pattern(regexp = "^01[0-9]{8,9}$", message = "올바른 전화번호 형식이 아닙니다. 01로 시작하고, 10자리 혹은 11자리 입니다. ex)01012345678")
  String phoneNumber;
}
