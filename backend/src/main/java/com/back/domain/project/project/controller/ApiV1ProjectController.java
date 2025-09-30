package com.back.domain.project.project.controller;

import com.back.domain.project.project.dto.ProjectDto;
import com.back.domain.project.project.dto.ProjectWriteReqBody;
import com.back.domain.project.project.entity.Project;
import com.back.domain.project.project.service.ProjectService;
import com.back.global.rsData.RsData;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
// @Tag(name="ApiV1ProjectController", description = "API 프로젝트 컨트롤러")
public class ApiV1ProjectController {
    private final ProjectService projectService;

    @PostMapping
    @Transactional
    public RsData<ProjectDto> write(@RequestBody ProjectWriteReqBody reqBody) {
        Project project = projectService.create(
                reqBody.title(),
                reqBody.summary(),
                reqBody.price(),
                reqBody.preferredCondition(),
                reqBody.payCondition(),
                reqBody.duration(),
                reqBody.description(),
                reqBody.deadline()
        );

        return new RsData<>(
                "201-1",
                "%d번 프로젝트가 생성되었습니다.".formatted(project.getId()),
                new ProjectDto(project)
        );
    }
}
