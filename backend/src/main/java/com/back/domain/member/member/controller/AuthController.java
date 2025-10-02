package com.back.domain.member.member.controller;

import com.back.domain.member.member.dto.MemberDto;
import com.back.domain.member.member.dto.MemberLoginReq;
import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.AuthService;
import com.back.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<MemberDto> login(@Valid @RequestBody MemberLoginReq reqBody) {
        Member member = authService.login(
                reqBody.username(),
                reqBody.password()
        );

        return new ApiResponse<>(
                "200-1",
                "%s님 환영합니다. 로그인이 완료되었습니다.".formatted(member.getName()),
                new MemberDto(member)
        );
    }
}
