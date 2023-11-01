package com.ssafy.eoullim.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name="animon")
@NoArgsConstructor
public class AnimonEntity extends BaseEntity {
    @Id
    @Column(name = "animon_id", columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column
    private String headImagePath;

    @Column
    private String bodyImagePath;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "child_id")
//    private ChildEntity childEntity;
}
