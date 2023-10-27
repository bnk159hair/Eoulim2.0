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
            regexp = "^(?=(.*[0-9]){1,})(?=(.*[a-z]){1,})(?=(.*[A-Z]){1,})(?=(.*[@#$%^&+=]){0,}).{8,20}$")
    private String newPassword;
}
