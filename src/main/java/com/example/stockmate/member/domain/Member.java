package com.example.stockmate.member.domain;

import com.example.stockmate.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"provider", "providerId"})})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String name;

    private String profileImage;

    @Column(nullable = false)
    private String provider; // kakao, google

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; // USER, ADMIN

    public enum Role {
        USER, ADMIN
    }

    public void updateNameAndImage(String name, String profileImage) {
        this.name = name;
        this.profileImage = profileImage;
    }
}