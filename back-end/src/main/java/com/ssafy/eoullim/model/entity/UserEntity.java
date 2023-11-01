package com.ssafy.eoullim.model.entity;

import com.ssafy.eoullim.model.User;
import com.ssafy.eoullim.model.UserRole;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity {
    @Id
    @Column(name = "user_id", columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, length = 17)
    private String name;

    @Column(nullable = false, length = 13)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Builder
    private UserEntity(Long id, String username, String password, String name, String phoneNumber, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public static UserEntity of(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }
}
