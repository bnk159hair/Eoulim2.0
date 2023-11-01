package com.ssafy.eoullim.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MatchStartRequest {
    private Long childId;
    private String name;
    private char gender;
    private String school;
    private Integer grade;
}
