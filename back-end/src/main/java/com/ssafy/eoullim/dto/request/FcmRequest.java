package com.ssafy.eoullim.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FcmRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String body;
    @NotBlank
    private String targetToken;
}
