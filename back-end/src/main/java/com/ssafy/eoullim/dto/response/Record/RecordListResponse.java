package com.ssafy.eoullim.dto.response.Record;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RecordListResponse {
    private Long id;
    private LocalDateTime createTime;
    private String videoPath;
    private String name;
    private String school;
    private String animonName;
}
