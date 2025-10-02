package com.back.domain.member.member.controller;

import com.back.domain.member.member.dto.MemberJoinReq;
import com.back.domain.member.member.dto.MemberDto;
import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import com.back.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService userService;

    @Transactional
    @PostMapping
    public ApiResponse<MemberDto> join(@Valid @RequestBody MemberJoinReq reqBody) {
        Member member = userService.join(
                reqBody.role(),
                reqBody.name(),
                reqBody.username(),
                reqBody.password(),
                reqBody.passwordConfirm(),
                reqBody.email()
        );

        return new ApiResponse<>(
                "201-1",
                "%s님 환영합니다. 회원가입이 완료되었습니다.".formatted(member.getName()),
                new MemberDto(member)
        );
    }
}
