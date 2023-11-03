package com.ssafy.eoullim.model.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "fcm_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmTokenEntity extends BaseEntity {
    @Id
    @Column(name = "fcm_token_id", columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildEntity child;

    @Column(nullable = false)
    private String token;

    @Builder
    public FcmTokenEntity(UserEntity user, ChildEntity child, String token) {
        this.user = user;
        this.child = child;
        this.token = token;
    }
}



