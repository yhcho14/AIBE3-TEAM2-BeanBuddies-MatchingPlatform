package com.back.domain.member.member.service;

import com.back.domain.member.member.dto.MemberJoinReq;
import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.repository.MemberRepository;
import com.back.global.exception.ServiceException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member join(@Valid MemberJoinReq reqBody) {
        //이미 사용중인 아이디인지 확인
        memberRepository.findByUsername(reqBody.username())
                .ifPresent(_member -> {
                    throw new ServiceException("409-1", "이미 존재하는 회원입니다.");
                });

        //비밀번호 확인
        if(!reqBody.password().equals(reqBody.passwordConfirm())) {
            throw new ServiceException("400-2", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(reqBody.password());

        //DTO -> ENTITY 변환
        Member member = new Member(
                reqBody.role(),
                reqBody.name(),
                reqBody.username(),
                encodedPassword,
                reqBody.email()
        );

        //DB 반영 후 반환
        return memberRepository.save(member);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
}
