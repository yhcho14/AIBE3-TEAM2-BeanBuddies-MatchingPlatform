package com.back.domain.freelancer.freelancer.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.back.domain.freelancer.freelancer.service.FreelancerService;
import com.back.domain.member.member.controller.MemberController;
import com.back.domain.member.member.service.MemberService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FreelancerControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private FreelancerService freelancerService;

    @Test
    @DisplayName("목록보기 응답 - 200")
    void t1() throws Exception{
        ResultActions resultActions = mvc
                .perform(
                        get("/freelancers")
                )
                .andDo(print());

        resultActions
                //실행처 확인
                .andExpect(handler().handlerType(FreelancerController.class))
                .andExpect(handler().methodName("getFreelancers"))

                //상태 코드 확인
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("목록보기 응답데이터 확인")
    void t1_data() throws Exception{
        ResultActions resultActions = mvc
                .perform(
                        get("/freelancers")
                )
                .andDo(print());

        // TODO: 데이터 확인
    }
}