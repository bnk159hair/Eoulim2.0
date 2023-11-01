package com.ssafy.eoullim.model.entity;

import com.ssafy.eoullim.model.Child;
import com.ssafy.eoullim.model.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "child")
public class ChildEntity extends BaseEntity {
    @Id
    @Column(name = "child_id", columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 17)
    private String name;

    @Column(nullable = false)
//    @Temporal(TemporalType.DATE)
    private LocalDate birth;

    @Column(columnDefinition = "CHAR(1)", nullable = false)
    private String gender;

    @Column(nullable = false, length = 20)
    private String school;

    @Column(nullable = false, columnDefinition = "TINYINT(1) UNSIGNED")
    @Range(min = 1, max = 6)
    private Short grade;

    @Enumerated(EnumType.STRING)
    private Status status = Status.OFF;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animon_id")
    private AnimonEntity animon;

    public static ChildEntity of(UserEntity user, Child child) {
        ChildEntity entity = new ChildEntity();
        entity.setName(child.getName());
        entity.setBirth(child.getBirth());
        entity.setGender(child.getGender());
        entity.setSchool(child.getSchool());
        entity.setGrade(child.getGrade());
        entity.setUser(user);
        return entity;
    }
}
