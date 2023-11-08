package com.ssafy.eoullim.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtherChild {
    private Long id;
    private String name;
    private String gender;
    private String school;
    private Short grade;
    private Animon profileAnimon;

    public static OtherChild fromChild(Child child) {
        return OtherChild.builder()
                .id(child.getId())
                .name(child.getName())
                .gender(child.getGender())
                .school(child.getSchool())
                .grade(child.getGrade())
                .profileAnimon(child.getProfileAnimon())
                .build();
    }
}

