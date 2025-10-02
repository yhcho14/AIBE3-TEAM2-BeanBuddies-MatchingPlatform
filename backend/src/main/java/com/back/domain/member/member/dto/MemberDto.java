package com.back.domain.member.member.dto;

import com.back.domain.member.member.constant.Role;
import com.back.domain.member.member.constant.MemberStatus;
import com.back.domain.member.member.entity.Member;

import java.time.LocalDateTime;

public record MemberDto(
        long id,
        String name,
        Role role,
        MemberStatus status,
        LocalDateTime createDate
) {
    public MemberDto(Member member) {
        this(
                member.getId(),
                member.getName(),
                member.getRole(),
                member.getStatus(),
                member.getCreateDate()
        );
    }
}
