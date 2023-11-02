package com.ssafy.eoullim.dto.response.Record;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class GuideDTO {
    private String content;
    private String timeline;
}
