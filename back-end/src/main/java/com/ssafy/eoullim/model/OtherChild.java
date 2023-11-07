package com.ssafy.eoullim.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OtherChild {
    private Long id;
    private String name;
    private String gender;
    private String school;
    private Short grade;
    private Animon profileAnimon;

    public static OtherChild fromChild(Child child) {
        return new OtherChild(
                child.getId(),
                child.getName(),
                child.getGender(),
                child.getSchool(),
                child.getGrade(),
                child.getProfileAnimon()
        );
    }
}

