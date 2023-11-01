package com.ssafy.eoullim.dto.response.Record;

import com.ssafy.eoullim.model.entity.ChildEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RecordResponse {
    private Long id;
    private String videoPath;
    private ChildEntity master;
    private ChildEntity participant;
}
