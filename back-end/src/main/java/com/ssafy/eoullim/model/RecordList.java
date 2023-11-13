package com.ssafy.eoullim.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RecordList {
    private Long id;
    private LocalDateTime createTime;
    private String videoPath;
    private String name;
    private String school;
    private String animonPath;
}
