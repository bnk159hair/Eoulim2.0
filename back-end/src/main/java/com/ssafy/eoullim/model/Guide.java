package com.ssafy.eoullim.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Guide {
    private String content;
    private String timeline;
}
