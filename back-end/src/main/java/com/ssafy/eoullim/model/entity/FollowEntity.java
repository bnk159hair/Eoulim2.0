package com.ssafy.eoullim.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name="follow")
@NoArgsConstructor
@AllArgsConstructor
public class FollowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildEntity child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private ChildEntity following;

    public static FollowEntity of(ChildEntity child, ChildEntity friend) {
        FollowEntity followEntity = new FollowEntity();
        followEntity.setChild(child);
        followEntity.setFollowing(friend);
        return followEntity;
    }
}
