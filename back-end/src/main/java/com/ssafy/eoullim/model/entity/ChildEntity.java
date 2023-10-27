package com.ssafy.eoullim.model.entity;

import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "child")
@NoArgsConstructor
public class ChildEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 4)
    private String name;

    @Column(nullable = false)
//    @Temporal(TemporalType.DATE)
    private LocalDate birth;

    @Column(nullable = false, length = 1)
    private char gender;

    @Column(nullable = false, length = 20)
    private String school;

    @Column(nullable = false)
    private Integer grade;

    @Enumerated(EnumType.STRING)
    private Status status = Status.OFF;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animon_id")
    private AnimonEntity animon;

//    public static ChildEntity of(UserEntity user, Child child) {
//        ChildEntity entity = new ChildEntity();
//        entity.setName(child.getName());
//        entity.setBirth(child.getBirth());
//        entity.setGender(child.getGender());
//        entity.setSchool(child.getSchool());
//        entity.setGrade(child.getGrade());
//        entity.setUser(user);
//        return entity;
//    }
}
