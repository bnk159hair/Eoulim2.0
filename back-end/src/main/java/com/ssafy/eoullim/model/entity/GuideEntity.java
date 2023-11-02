package com.ssafy.eoullim.model.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name="guide")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuideEntity extends BaseEntity {
    @Id
    @Column(name = "guide_id", columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;
}
