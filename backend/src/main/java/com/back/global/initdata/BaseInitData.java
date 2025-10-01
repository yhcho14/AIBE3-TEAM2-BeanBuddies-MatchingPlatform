package com.back.global.initdata;

import com.back.domain.common.interest.service.InterestService;
import com.back.domain.common.skill.service.SkillService;
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

    private final SkillService skillService;
    private final InterestService interestService;

    @Bean
    ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        if (skillService.count() > 0) return;

        skillService.create("Java");
        skillService.create("Spring boot");
        skillService.create("React");

        interestService.create("웹 개발");
        interestService.create("모바일 앱");
        interestService.create("데이터 사이언스");
    }
}

