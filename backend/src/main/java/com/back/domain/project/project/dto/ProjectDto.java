package com.back.domain.project.project.dto;

import com.back.domain.project.project.constant.ProjectStatus;
import com.back.domain.project.project.entity.Project;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProjectDto(
        Long id,
        String title,
        String summary,
        String duration,
        BigDecimal price,
        String preferredCondition,
        String payCondition,
        String description,
        ProjectStatus status,
        LocalDateTime createDate,
        LocalDateTime modifyDate
) {
    public ProjectDto (Project project) {
        this(
                project.getId(),
                project.getTitle(),
                project.getSummary(),
                project.getDuration(),
                project.getPrice(),
                project.getPreferredCondition(),
                project.getPayCondition(),
                project.getDescription(),
                project.getStatus(),
                project.getCreateDate(),
                project.getModifyDate()
        );
    }
}
