package com.ssafy.eoullim.model.entity;

import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.OtherChild;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@Table(name = "child")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChildEntity extends BaseEntity {
    @Id
    @Column(name = "child_id", columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 17)
    private String name;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(columnDefinition = "CHAR(1)", nullable = false)
    private String gender;

    @Column(nullable = false, length = 20)
    private String school;

    @Column(nullable = false, columnDefinition = "TINYINT(1) UNSIGNED")
    @Range(min = 1, max = 6)
    private Short grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animon_id")
    private AnimonEntity profileAnimon;

    @Builder
    public ChildEntity(
            Long id,
            String name,
            LocalDate birth,
            String gender,
            String school,
            Short grade,
            UserEntity user,
            AnimonEntity profileAnimon) {
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.school = school;
        this.grade = grade;
        this.user = user;
        this.profileAnimon = profileAnimon;
    }

    public static ChildEntity of(Child child) {
        return ChildEntity.builder()
                .id(child.getId())
                .name(child.getName())
                .birth(child.getBirth())
                .gender(child.getGender())
                .school(child.getSchool())
                .grade(child.getGrade())
                .user(UserEntity.of(child.getUser()))
                .profileAnimon(AnimonEntity.of(child.getProfileAnimon()))
                .build();
    }

    public static ChildEntity of(OtherChild otherChild) {
        return ChildEntity.builder()
                .id(otherChild.getId())
                .name(otherChild.getName())
                .gender(otherChild.getGender())
                .school(otherChild.getSchool())
                .grade(otherChild.getGrade())
                .profileAnimon(AnimonEntity.of(otherChild.getProfileAnimon()))
                .build();
    }

    public static ChildEntity of(UserEntity user, Child child) {
        return ChildEntity.builder()
                .name(child.getName())
                .birth(child.getBirth())
                .gender(child.getGender())
                .school(child.getSchool())
                .grade(child.getGrade())
                .user(user)
                .build();
    }

    public void setProfileAnimon(AnimonEntity profileAnimon) {
        this.profileAnimon = profileAnimon;
    }

    public void updateInfo(Child child) {
        this.name = child.getName();
        this.birth = child.getBirth();
        this.gender = child.getGender();
        this.school = child.getSchool();
        this.grade = child.getGrade();
    }
}
