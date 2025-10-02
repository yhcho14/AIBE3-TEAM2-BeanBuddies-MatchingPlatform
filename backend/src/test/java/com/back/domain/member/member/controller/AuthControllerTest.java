package com.back.domain.member.member.controller;

import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import jakarta.servlet.http.Cookie;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("로그인 성공 - 헤더, 쿠키 검증")
    void t1_login() throws Exception {

        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                                "username" : "client1",
                                                "password" : "1234"
                                        }
                                        """)
                )
                .andDo(print());

        Member member = memberService.findByUsername("client1").get();

        resultActions
                //실행처 확인
                .andExpect(handler().handlerType(AuthController.class))
                .andExpect(handler().methodName("login"))

                //상태 코드 확인
                .andExpect(status().isOk())

                //응답 데이터 확인
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("%s님 환영합니다. 로그인이 완료되었습니다.".formatted(member.getName())))
                .andExpect(jsonPath("$.data.id").value(member.getId()))
                .andExpect(jsonPath("$.data.name").value(member.getName()))
                .andExpect(jsonPath("$.data.role").value(member.getRole().name()))
                .andExpect(jsonPath("$.data.status").value(member.getStatus().name()))
                .andExpect(jsonPath("$.data.createDate").value(Matchers.startsWith(member.getCreateDate().toString().substring(0, 20))))

                //헤더 확인
                .andExpect(header().exists("Authorization"))
                .andExpect(header().string("Authorization", Matchers.startsWith("Bearer ")))

                //쿠키 확인
                .andExpect(result -> {
                    Cookie refreshToken = result.getResponse().getCookie("refreshToken");
                    assertThat(refreshToken.getPath()).isEqualTo("/");
                    assertThat(refreshToken.getAttribute("HttpOnly")).isEqualTo("true");
                });
    }

    @Test
    @DisplayName("로그인 실패 : username 존재 X")
    void t2_login_exception() throws Exception {

        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                                "username" : "client12",
                                                "password" : "1234"
                                        }
                                        """)
                )
                .andDo(print());


        resultActions
                //실행처 확인
                .andExpect(handler().handlerType(AuthController.class))
                .andExpect(handler().methodName("login"))

                //상태 코드 확인
                .andExpect(status().isUnauthorized())

                //응답 데이터 확인
                .andExpect(jsonPath("$.resultCode").value("401-1"))
                .andExpect(jsonPath("$.msg").value("존재하지 않는 회원입니다."));

    }

    @Test
    @DisplayName("로그인 실패 : 비밀번호 불일치")
    void t3_login_exception() throws Exception {

        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                                "username" : "client1",
                                                "password" : "qwerqwer"
                                        }
                                        """)
                )
                .andDo(print());


        resultActions
                //실행처 확인
                .andExpect(handler().handlerType(AuthController.class))
                .andExpect(handler().methodName("login"))

                //상태 코드 확인
                .andExpect(status().isUnauthorized())

                //응답 데이터 확인
                .andExpect(jsonPath("$.resultCode").value("401-2"))
                .andExpect(jsonPath("$.msg").value("비밀번호가 일치하지 않습니다."));

    }
}
