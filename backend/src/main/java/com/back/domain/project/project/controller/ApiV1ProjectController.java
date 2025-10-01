package com.back.domain.project.project.controller;

import com.back.domain.common.interest.dto.InterestDto;
import com.back.domain.common.interest.service.InterestService;
import com.back.domain.common.skill.dto.SkillDto;
import com.back.domain.common.skill.service.SkillService;
import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import com.back.domain.project.project.dto.ProjectDto;
import com.back.domain.project.project.dto.ProjectModifyReqBody;
import com.back.domain.project.project.dto.ProjectWriteReqBody;
import com.back.domain.project.project.entity.Project;
import com.back.domain.project.project.service.ProjectService;
import com.back.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
// @Tag(name="ApiV1ProjectController", description = "API 프로젝트 컨트롤러")
public class ApiV1ProjectController {
    private final ProjectService projectService;
    private final MemberService memberService;
    private final SkillService skillService;
    private final InterestService interestService;

    @PostMapping
    @Transactional
    public ApiResponse<ProjectDto> write(@Valid @RequestBody ProjectWriteReqBody reqBody) {
        // 임시로 회원 데이터 1개 가져옴
        Member member = memberService.findByUsername("client1").get();

        Project project = projectService.create(
                member,
                reqBody.title(),
                reqBody.summary(),
                reqBody.price(),
                reqBody.preferredCondition(),
                reqBody.payCondition(),
                reqBody.workingCondition(),
                reqBody.duration(),
                reqBody.description(),
                reqBody.deadline(),
                reqBody.skills(),
                reqBody.interests()
        );

        // 저장된 Project의 skill, interest 정보 가져오기
        List<SkillDto> skillDtoList = skillService.findByProjectId(project.getId());
        List<InterestDto> interestDtoList = interestService.findByProjectId(project.getId());

        return new ApiResponse<>(
                "201-1",
                "%d번 프로젝트가 생성되었습니다.".formatted(project.getId()),
                // skill, interest 데이터 넣기
                new ProjectDto(project, skillDtoList, interestDtoList)
        );
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ApiResponse<Void> delete(@PathVariable long id) {
        Project project = projectService.findById(id);

        projectService.delete(project);

        return new ApiResponse<>("200-1", "%d번 프로젝트가 삭제되었습니다.".formatted(id));
    }

    @PatchMapping("/{id}")
    @Transactional
    public ApiResponse<ProjectDto> modify(
            @PathVariable long id,
            @Valid @RequestBody ProjectModifyReqBody reqBody
            ) {
        Project project = projectService.findById(id);
        projectService.update(
                project,
                reqBody.title(),
                reqBody.summary(),
                reqBody.price(),
                reqBody.preferredCondition(),
                reqBody.payCondition(),
                reqBody.workingCondition(),
                reqBody.duration(),
                reqBody.description(),
                reqBody.deadline(),
                reqBody.status(),
                reqBody.skills(),
                reqBody.interests()
        );

        // ProjectSkill, ProjectInterest 연관 데이터 DTO로 변환
        List<SkillDto> skillDtoList = skillService.findByProjectId(project.getId());
        List<InterestDto> interestDtoList = interestService.findByProjectId(project.getId());

        return new ApiResponse<>(
                "200-1",
                "%d번 프로젝트가 수정되었습니다.".formatted(project.getId()),
                new ProjectDto(project, skillDtoList, interestDtoList)
        );
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ProjectDto getItem(@PathVariable long id) {
        Project project = projectService.findById(id);
        List<SkillDto> skillDtoList = skillService.findByProjectId(id);
        List<InterestDto> interestDtoList = interestService.findByProjectId(id);
        return new ProjectDto(project, skillDtoList, interestDtoList);
    }
}
