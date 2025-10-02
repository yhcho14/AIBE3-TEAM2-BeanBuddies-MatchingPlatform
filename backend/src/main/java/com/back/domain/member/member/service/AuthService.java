package com.back.domain.member.member.service;

import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.repository.MemberRepository;
import com.back.global.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public Member login(String username, String password) {
        //가입된 ID인지 확인
        Member member = memberService.findByUsername(username)
                .orElseThrow(() -> new ServiceException("401-1", "존재하지 않는 회원입니다."));

        //password 일치하는지 확인
        memberService.checkPassword(member, password);

        return member;
    }
}
