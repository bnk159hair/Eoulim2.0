package com.ssafy.eoullim.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Table(name="animon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnimonEntity extends BaseEntity {
    @Id
    @Column(name = "animon_id", columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private String headImagePath;

    @Column(nullable = false)
    private String bodyImagePath;

    @Builder
    public AnimonEntity(String name, String headImagePath, String bodyImagePath) {
        this.name = name;
        this.headImagePath = headImagePath;
        this.bodyImagePath = bodyImagePath;
    }
}
