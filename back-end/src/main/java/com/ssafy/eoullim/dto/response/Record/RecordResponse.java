package com.ssafy.eoullim.dto.response.Record;

import com.ssafy.eoullim.model.entity.ChildEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RecordResponse {
    private Long id;
    private LocalDateTime createTime;
    private String videoPath;
    private String name;
    private String school;
    private String animonName;
    private List<GuideDTO> guideInfo;

}
