package com.ssafy.eoullim.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Record {
    private Long id;
    private LocalDateTime createTime;
    private String videoPath;
    private String name;
    private String school;
    private String animonPath;
    private List<Guide> guideInfo;

}
