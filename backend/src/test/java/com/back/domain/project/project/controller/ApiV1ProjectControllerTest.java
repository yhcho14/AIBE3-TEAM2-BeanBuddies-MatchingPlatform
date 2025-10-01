package com.back.domain.project.project.controller;

import com.back.domain.project.project.entity.Project;
import com.back.domain.project.project.entity.ProjectInterest;
import com.back.domain.project.project.entity.ProjectSkill;
import com.back.domain.project.project.service.ProjectService;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                    "workingCondition": "업무 조건",
                    "description": "상세 설명",
                    "deadline": "2025-12-31T23:59:59",
                    "skills": [1, 2],
                    "interests": [1, 2]
                }
                """)
        ).andDo(print());

        // DB에서 가장 최근 프로젝트 조회
        Project project = projectService.findLatest().orElseThrow(
                () -> new AssertionError("프로젝트가 생성되지 않았습니다.")
        );

        // 응답 검증
        resultActions
                .andExpect(status().isCreated())
                .andExpect(handler().handlerType(ApiV1ProjectController.class))
                .andExpect(handler().methodName("write"))
                .andExpect(jsonPath("$.resultCode").value("201-1"))
                .andExpect(jsonPath("$.msg").value("%d번 프로젝트가 생성되었습니다.".formatted(project.getId())))
                .andExpect(jsonPath("$.data.id").value(project.getId()))
                .andExpect(jsonPath("$.data.title").value("테스트 프로젝트"))
                .andExpect(jsonPath("$.data.id").value(project.getId()))
                .andExpect(jsonPath("$.data.title").value("테스트 프로젝트"))
                .andExpect(jsonPath("$.data.summary").value("테스트 요약"))
                .andExpect(jsonPath("$.data.duration").value("1개월"))
                .andExpect(jsonPath("$.data.price").value(1000000))
                .andExpect(jsonPath("$.data.preferredCondition").value("우대 조건"))
                .andExpect(jsonPath("$.data.payCondition").value("급여 조건"))
                .andExpect(jsonPath("$.data.workingCondition").value("업무 조건"))
                .andExpect(jsonPath("$.data.description").value("상세 설명"))
                .andExpect(jsonPath("$.data.deadline").value("2025-12-31T23:59:59"))
                .andExpect(jsonPath("$.data.ownerName").value("클라이언트1"))


                // Skills 검증
                .andExpect(jsonPath("$.data.skills").isArray())
                .andExpect(jsonPath("$.data.skills.length()").value(2))
                .andExpect(jsonPath("$.data.skills[0].id").value(1))
                .andExpect(jsonPath("$.data.skills[0].name").value("Java"))
                .andExpect(jsonPath("$.data.skills[1].id").value(2))
                .andExpect(jsonPath("$.data.skills[1].name").value("Spring boot"))

                // Interests 검증
                .andExpect(jsonPath("$.data.interests").isArray())
                .andExpect(jsonPath("$.data.interests.length()").value(2))
                .andExpect(jsonPath("$.data.interests[0].id").value(1))
                .andExpect(jsonPath("$.data.interests[0].name").value("웹 개발"))
                .andExpect(jsonPath("$.data.interests[1].id").value(2))
                .andExpect(jsonPath("$.data.interests[1].name").value("모바일 앱"));
    }

    @Test
    @DisplayName("프로젝트 등록 (제목 누락)")
    @WithMockUser(username = "user1", roles = {"ADMIN"})
    void t1_1() throws Exception {
        ResultActions resultActions = mvc.perform(
                post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content("""
                {
                    "title": "",
                    "summary": "테스트 요약",
                    "duration": "1개월",
                    "price": 1000000,
                    "preferredCondition": "우대 조건",
                    "payCondition": "급여 조건",
                    "workingCondition": "업무 조건",
                    "description": "상세 설명",
                    "deadline": "2025-12-31T23:59:59",
                    "skills": [1, 2],
                    "interests": [1, 2]
                }
                """)
        ).andDo(print());

        // 응답 검증
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value("400-1"))
                .andExpect(jsonPath("$.msg").value("title-NotBlank-제목은 필수입니다."));
    }

    @Test
    @DisplayName("프로젝트 삭제")
    @WithMockUser(username = "user1", roles = {"ADMIN"})
    void t2() throws  Exception {
        long id = 1;
        Project project = projectService.findById(id);
        ResultActions resultActions = mvc.perform(
                delete("/api/v1/projects/" + id)
                        .with(csrf())
        ).andDo(print());

        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1ProjectController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("%d번 프로젝트가 삭제되었습니다.".formatted(id)));

        // 연관 ProjectSkill/ProjectInterest 삭제 확인
        assertThat(projectService.findProjectSkillAllByProject(project)).isEmpty();
        assertThat(projectService.findProjectInterestAllByProject(project)).isEmpty();
    }

    @Test
    @DisplayName("프로젝트 수정")
    @WithMockUser(username = "user1", roles = {"ADMIN"})
    void t3() throws  Exception {
        long id = 1;
        Project project = projectService.findById(id);

        ResultActions resultActions = mvc.perform(
                patch("/api/v1/projects/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content("""
                {
                    "title": "테스트 프로젝트 update",
                    "summary": "테스트 요약 update",
                    "duration": "2개월",
                    "price": 10000000,
                    "preferredCondition": "우대 조건  update",
                    "payCondition": "급여 조건  update",
                    "workingCondition": "업무 조건  update",
                    "description": "상세 설명  update",
                    "deadline": "2026-12-31T23:59:59",
                    "skills": [1, 2, 3],
                    "interests": [1, 2, 3]
                }
                """)
        ).andDo(print());

        // 응답 검증
        resultActions
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ApiV1ProjectController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("%d번 프로젝트가 수정되었습니다.".formatted(project.getId())));

        // DB에서 실제 연결 상태 조회
        List<ProjectSkill> updatedSkills = projectService.findProjectSkillAllByProject(project);
        List<ProjectInterest> updatedInterests = projectService.findProjectInterestAllByProject(project);

        // skill 검증
        assertThat(updatedSkills)
                .extracting(ps -> ps.getSkill().getId())
                .containsExactlyInAnyOrder(1L, 2L, 3L);

        // interest 검증
        assertThat(updatedInterests)
                .extracting(pi -> pi.getInterest().getId())
                .containsExactlyInAnyOrder(1L, 2L, 3L);
    }
}