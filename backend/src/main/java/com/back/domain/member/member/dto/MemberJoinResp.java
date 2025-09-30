package com.back.domain.member.member.dto;

import com.back.domain.member.member.constant.Role;
import com.back.domain.member.member.constant.Status;
import com.back.domain.member.member.entity.Member;

import java.time.LocalDateTime;

public record MemberJoinResp(
        long id,
        String name,
        Role role,
        Status status,
        LocalDateTime createDate
) {
    public MemberJoinResp(Member member) {
        this(
                member.getId(),
                member.getName(),
                member.getRole(),
                member.getStatus(),
                member.getCreateDate()
        );
    }
}
