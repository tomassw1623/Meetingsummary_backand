// com/example/meetingsummary/entity/User.java

package com.example.meetingsummary.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // providerId가 이제 고유한 식별자 역할을 합니다.
    @Column(nullable = false, unique = true)
    private String providerId;

    @Column(nullable = false)
    private String name;

    @Column // 이메일은 이제 필수가 아닙니다.
    private String email;

    @Column
    private String picture;

    @Builder
    public User(String providerId, String name, String email, String picture) {
        this.providerId = providerId;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }
}