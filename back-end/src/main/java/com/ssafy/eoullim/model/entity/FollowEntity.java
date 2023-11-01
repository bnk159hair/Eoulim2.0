package com.ssafy.eoullim.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="follow")
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

    public static FollowEntity of(ChildEntity child, ChildEntity friend) {
        FollowEntity followEntity = new FollowEntity();
        followEntity.setChild(child);
        followEntity.setFollowingChild(friend);
        return followEntity;
    }
}
