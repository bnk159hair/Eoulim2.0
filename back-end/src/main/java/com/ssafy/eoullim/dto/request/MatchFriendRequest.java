package com.ssafy.eoullim.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MatchFriendRequest {
  @NotNull private Long childId;

  @Pattern(
      regexp =
          "^(20[2-9][0-9](0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])([01][0-9]|2[0-3])([0-5][0-9]){2})_\\d{1,}$",
      message = "yyyyMMddHHmmss_숫자 형식의 ID 형식입니다.")
  private String sessionId;

  @NotNull private Long friendId;
}
