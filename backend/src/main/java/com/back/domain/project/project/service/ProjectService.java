package com.back.domain.project.project.service;

import com.back.domain.common.interest.entity.Interest;
import com.back.domain.common.interest.repository.InterestRepository;
import com.back.domain.common.skill.entity.Skill;
import com.back.domain.common.skill.repository.SkillRepository;
import com.back.domain.project.project.entity.Project;
import com.back.domain.project.project.entity.ProjectInterest;
import com.back.domain.project.project.entity.ProjectSkill;
import com.back.domain.project.project.repository.ProjectInterestRepository;
import com.back.domain.project.project.repository.ProjectRepository;
import com.back.domain.project.project.repository.ProjectSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    private final InterestRepository interestRepository;
    private final SkillRepository skillRepository;

    private final ProjectInterestRepository projectInterestRepository;
    private final ProjectSkillRepository projectSkillRepository;

    public Project create(
            String title,
            String summary,
            BigDecimal price,
            String preferredCondition,
            String payCondition,
            String duration,
            String description,
            LocalDateTime deadline,
            List<Long> skills,
            List<Long> interests
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
        projectRepository.save(project);

        List<Skill> skillList = skillRepository.findAllById(skills);
        projectSkillRepository.saveAll(
                skillList.stream()
                        .map(skill -> new ProjectSkill(project, skill))
                        .toList()
        );

        List<Interest> interestList = interestRepository.findAllById(interests);
        projectInterestRepository.saveAll(
                interestList.stream()
                        .map(interest -> new ProjectInterest(project, interest))
                        .toList()
        );

        return project;
    }

    public Optional<Project> findLatest() {
        return projectRepository.findFirstByOrderByIdDesc();
    }
}
