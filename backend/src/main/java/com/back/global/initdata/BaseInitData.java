package com.back.global.initdata;

import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Configuration
public class BaseInitData {

    @Autowired
    @Lazy
    private BaseInitData self;
    private final MemberService memberService;

    @Bean
    ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.work1();
            self.addMember();
        };
    }

    @Transactional
    public void work1() {
    }

    @Transactional
    public void addMember() {

        if(memberService.count() > 0) return;

        //임의 데이터 추가
        Member admin = memberService.join("ADMIN", "관리자", "admin", "1234", "1234", "test@test.com");
        Member client1 = memberService.join("CLIENT", "클라이언트1", "client1", "1234", "1234", "test@test.com");
        Member client2 = memberService.join("CLIENT", "클라이언트2", "client2", "1234", "1234", "test@test.com");
        Member freelancer1 = memberService.join("FREELANCER", "프리랜서1", "freelancer1", "1234", "1234", "test@test.com");
        Member freelancer2 = memberService.join("FREELANCER", "프리랜서2", "freelancer2", "1234", "1234", "test@test.com");

        //클라이언트2, 프리랜서2는 활동 정지 상태로 변경
        memberService.changeStatus(client2, "INACTIVE");
        memberService.changeStatus(freelancer2, "INACTIVE");
    }
}

