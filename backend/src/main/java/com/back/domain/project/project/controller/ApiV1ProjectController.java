package com.back.domain.project.project.controller;

import com.back.domain.common.interest.dto.InterestDto;
import com.back.domain.common.interest.service.InterestService;
import com.back.domain.common.skill.dto.SkillDto;
import com.back.domain.common.skill.service.SkillService;
import com.back.domain.project.project.dto.ProjectDto;
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
    private final SkillService skillService;
    private final InterestService interestService;

    @PostMapping
    @Transactional
    public ApiResponse<ProjectDto> write(@Valid @RequestBody ProjectWriteReqBody reqBody) {
        Project project = projectService.create(
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

}
