package com.back.domain.project.project.controller;

import com.back.domain.project.project.entity.Project;
import com.back.domain.project.project.service.ProjectService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ApiV1ProjectControllerTest {
    @Autowired
    private MockMvc mvc; // MockMvc를 주입받습니다.
    @Autowired
    ProjectService projectService;

    @Test
    @DisplayName("프로젝트 생성")
    @WithMockUser(username = "user1", roles = {"ADMIN"})
    void t1() throws  Exception {
        ResultActions resultActions = mvc.perform(
                post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content("""
                {
                    "title": "테스트 프로젝트",
                    "summary": "테스트 요약",
                    "duration": "1개월",
                    "price": 1000000,
                    "preferredCondition": "우대 조건",
                    "payCondition": "급여 조건",
                    "description": "상세 설명",
                    "deadline": "2025-12-31T23:59:59"
                }
                """)
        ).andDo(print());

        // DB에서 가장 최근 프로젝트 조회
        Project project = projectService.findLatest().orElseThrow(
                () -> new AssertionError("프로젝트가 생성되지 않았습니다.")
        );

        // 응답 검증
        resultActions
                .andExpect(handler().handlerType(ApiV1ProjectController.class))
                .andExpect(handler().methodName("write")) // 컨트롤러 메서드명에 맞게 수정
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultCode").value("201-1"))
                .andExpect(jsonPath("$.msg").value("%d번 프로젝트가 생성되었습니다.".formatted(project.getId())))
                .andExpect(jsonPath("$.data.id").value(project.getId()))
                .andExpect(jsonPath("$.data.createDate").value(Matchers.startsWith(project.getCreateDate().toString().substring(0, 20))))
                .andExpect(jsonPath("$.data.modifyDate").value(Matchers.startsWith(project.getModifyDate().toString().substring(0, 20))))
                .andExpect(jsonPath("$.data.title").value("테스트 프로젝트"))
                .andExpect(jsonPath("$.data.summary").value("테스트 요약"))
                .andExpect(jsonPath("$.data.duration").value("1개월"))
                .andExpect(jsonPath("$.data.price").value(1000000))
                .andExpect(jsonPath("$.data.preferredCondition").value("우대 조건"))
                .andExpect(jsonPath("$.data.payCondition").value("급여 조건"))
                .andExpect(jsonPath("$.data.description").value("상세 설명"))
                .andExpect(jsonPath("$.data.status").value("OPEN"));
    }
}