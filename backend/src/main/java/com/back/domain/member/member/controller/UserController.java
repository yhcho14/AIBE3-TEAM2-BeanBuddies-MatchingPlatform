package com.back.domain.member.member.controller;

import com.back.domain.member.member.dto.MemberJoinReq;
import com.back.domain.member.member.dto.MemberJoinResp;
import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.UserService;
import com.back.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Transactional
    @PostMapping
    public ApiResponse<MemberJoinResp> join(@Valid @RequestBody MemberJoinReq reqBody) {
        Member member = userService.join(reqBody);

        return new ApiResponse<>(
                "201-1",
                "%s님 환영합니다. 회원가입이 완료되었습니다.".formatted(member.getName()),
                new MemberJoinResp(member)
        );
    }
}
