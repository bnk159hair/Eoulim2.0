package com.ssafy.eoullim.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Room {
    private String sessionId;
    private String recordingId;
    private Long childOne;
    private Long childTwo;
    private List<Integer> random;
    private List<Integer> guideSeq;
    private List<String> timeline;
}
