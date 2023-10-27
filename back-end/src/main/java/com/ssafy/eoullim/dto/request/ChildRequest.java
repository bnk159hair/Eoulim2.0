package com.ssafy.eoullim.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChildRequest {
    // 자녀 정보
    @NotBlank
    @Length(max = 4, message = "이름은 4자 이하 이어야 합니다.")
    private String name;        // front 단에서 비동기 처리

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "생년월일은 'yyyy-MM-dd' 형식이어야 합니다.")
    private LocalDate birth;

    @Pattern(regexp = "^(M|W)$", message = "성별은 'M' 또는 'W' 중 하나여야 합니다.")
    private char gender;        //남자는 M, 여자는 W

    @NotBlank
    @Pattern(regexp = "^.+초등학교$", message = "학교 이름은 '초등학교'로 끝나야 합니다.")
    @Length(max = 20, message = "학교 이름은 20자 이하여야 합니다.")
    private String school;

    @Range(min = 1, max = 6, message = "학년은 1에서 6 사이여야 합니다.")
    private Integer grade;          // front 단에서 1~3 선택할 수 있는 드롭다운
}

