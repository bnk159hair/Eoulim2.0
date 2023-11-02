package com.ssafy.eoullim.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Table(name="follow")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowEntity extends BaseEntity{
    @Id
    @Column(name = "follow_id", columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildEntity child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_child_id", nullable = false)
    private ChildEntity followingChild;

    @Builder
    public FollowEntity(ChildEntity child, ChildEntity followingChild) {
        this.child = child;
        this.followingChild = followingChild;
    }

    public static FollowEntity of(ChildEntity child, ChildEntity followingChild) {
        return FollowEntity.builder()
                .child(child)
                .followingChild(followingChild)
                .build();
    }
}
