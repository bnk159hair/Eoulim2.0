package com.ssafy.eoullim.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserModifyRequest {
    @NotBlank
    private String curPassword;
    //    @Pattern(regexp = "[a-zA-Z0-9]{8,20}")
    @Pattern(
            regexp = "^(?=(.*[0-9]){1,})(?=(.*[a-z]){1,})(?=(.*[A-Z]){1,})(?=(.*[@#$%^&+=]){0,}).{8,20}$",
            message =
                    "문자열은 숫자([0-9]), 소문자([a-z]), 대문자([A-Z]), 특수 문자([@#$%^&+=]) 중 3가지 이상을 포함해야 합니다.\n"
                            + "문자열은 총 길이가 8자 이상 20자 이하 이어야 합니다.")
    private String newPassword;
}
