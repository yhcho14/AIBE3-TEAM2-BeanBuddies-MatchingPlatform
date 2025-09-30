package com.back.domain.project.project.service;

import com.back.domain.project.project.entity.Project;
import com.back.domain.project.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public Project create(
            String title,
            String summary,
            BigDecimal price,
            String preferredCondition,
            String payCondition,
            String duration,
            String description,
            LocalDateTime deadline
    ) {
        Project project = new Project(
                title,
                summary,
                price,
                preferredCondition,
                payCondition,
                duration,
                description,
                deadline
                );
        return projectRepository.save(project);
    }

    public Optional<Project> findLatest() {
        return projectRepository.findFirstByOrderByIdDesc();
    }
}
