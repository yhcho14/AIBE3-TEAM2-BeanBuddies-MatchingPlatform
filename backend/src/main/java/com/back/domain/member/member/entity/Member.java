package com.back.domain.member.member.entity;

import com.back.domain.member.member.constant.Role;
import com.back.domain.member.member.constant.Status;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Member extends BaseEntity {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String username;

    private String password;

    private String email;

    //private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    private LocalDateTime deleteDate;

    public Member(String role, String name, String username, String password, String email) {
        this.role = Role.valueOf(role);
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public void changeStatus(String status) {
        this.status = Status.valueOf(status);
    }
}
