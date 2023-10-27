package com.ssafy.eoullim.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequest {
  @NotBlank
  @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9]{2,19}") // 첫 글자는 영어 대소문자, 그 뒤는 영어 대소문자 혹은 숫자 3~20자
  String username;

  @NotBlank
  //    @Pattern(regexp = "[a-zA-Z0-9]{8,20}")
  @Pattern(
      regexp = "^(?=(.*[0-9]){1,})(?=(.*[a-z]){1,})(?=(.*[A-Z]){1,})(?=(.*[@#$%^&+=]){0,}).{8,20}$")
  String password;

  @NotBlank String name;

  @NotBlank
  @Pattern(regexp = "01[0-9]-[0-9]{4}-[0-9]{4}")
  String phoneNumber;
}
