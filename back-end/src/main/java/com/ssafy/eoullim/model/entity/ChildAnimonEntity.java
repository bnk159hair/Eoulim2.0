package com.ssafy.eoullim.model.entity;


import com.ssafy.eoullim.model.Animon;
import com.ssafy.eoullim.model.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "child_animon")
public class ChildAnimonEntity extends BaseEntity{
    @Id
    @Column(name="child_animon_id", columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private ChildEntity child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animon_id")
    private AnimonEntity animon;

    @Builder
    public ChildAnimonEntity(Child child, Animon animon) {
        this.child = ChildEntity.of(child);
        this.animon = AnimonEntity.of(animon);
    }
}