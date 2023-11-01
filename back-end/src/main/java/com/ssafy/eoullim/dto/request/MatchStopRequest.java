package com.ssafy.eoullim.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MatchStopRequest {
    private String sessionId;
    private List<Integer> guideSeq;
    private List<String> timeline;
}
