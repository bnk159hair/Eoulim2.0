package com.ssafy.eoullim.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MatchStartRequest {

    @NotNull
    private Long childId;

    @NotBlank
    @Pattern(regexp = "^[가-힣]{2,17}$", message = "이름은 17자 이하의 한글 문자여야 합니다.")
    private String name; // front 단에서 비동기 처리

    @Pattern(regexp = "^(M|W)$", message = "성별은 'M' 또는 'W' 중 하나여야 합니다.")
    private String gender; // 남자는 M, 여자는 W

    @NotBlank
//  @Pattern(regexp = "^.+초등학교$", message = "학교 이름은 '초등학교'로 끝나야 합니다.")
    @Length(max = 20, message = "학교 이름은 20자 이하여야 합니다.")
    private String school;

    @Range(min = 1, max = 6, message = "학년은 1에서 6 사이여야 합니다.")
    private Short grade;
}
