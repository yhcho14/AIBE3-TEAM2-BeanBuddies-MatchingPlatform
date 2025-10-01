package com.back.domain.project.project.service;

import com.back.domain.common.interest.entity.Interest;
import com.back.domain.common.interest.repository.InterestRepository;
import com.back.domain.common.skill.entity.Skill;
import com.back.domain.common.skill.repository.SkillRepository;
import com.back.domain.member.member.entity.Member;
import com.back.domain.project.project.constant.ProjectStatus;
import com.back.domain.project.project.entity.Project;
import com.back.domain.project.project.entity.ProjectInterest;
import com.back.domain.project.project.entity.ProjectSkill;
import com.back.domain.project.project.repository.ProjectInterestRepository;
import com.back.domain.project.project.repository.ProjectRepository;
import com.back.domain.project.project.repository.ProjectSkillRepository;
import com.back.global.exception.ServiceException;
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

    public long count() {
        return projectRepository.count();
    }

    public Project create(
            Member owner,
            String title,
            String summary,
            BigDecimal price,
            String preferredCondition,
            String payCondition,
            String workingCondition,
            String duration,
            String description,
            LocalDateTime deadline,
            List<Long> skills,
            List<Long> interests
    ) {
        Project project = new Project(
                owner,
                title,
                summary,
                price,
                preferredCondition,
                payCondition,
                workingCondition,
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

    public Project findById(long id) {
        return projectRepository.findById(id).orElseThrow(
                () -> new ServiceException("401-1", "해당 프로젝트가 존재하지 않습니다.")
        );
    }

    public void delete(Project project) {
        // ProjectSkill, ProjectInterest 우선 삭제
        projectSkillRepository.deleteAllByProject(project);
        projectInterestRepository.deleteAllByProject(project);
        projectRepository.delete(project);
    }

    public List<ProjectSkill> findProjectSkillAllByProject(Project project) {
        return projectSkillRepository.findAllByProject(project);
    }

    public List<ProjectInterest> findProjectInterestAllByProject(Project project) {
        return projectInterestRepository.findAllByProject(project);
    }

    public void update(
            Project project,
            String title,
            String summary,
            BigDecimal price,
            String preferredCondition,
            String payCondition,
            String workingCondition,
            String duration,
            String description,
            LocalDateTime deadline,
            ProjectStatus status,
            List<Long> skills,
            List<Long> interests
    ) {
        project.modify(
                title,
                summary,
                price,
                preferredCondition,
                payCondition,
                workingCondition,
                duration,
                description,
                deadline,
                status
        );

        // ProjectSkill, ProjectInterest 수정 (삭제 후 추가)
        // 1. ProjectSkill, ProjectInterest 삭제
        projectSkillRepository.deleteAllByProject(project);
        projectInterestRepository.deleteAllByProject(project);

        // 2. ProjectSkill, ProjectInterest 새로 추가
        List<Skill> skillList = skillRepository.findAllById(skills);
        for (Skill skill : skillList) {
            projectSkillRepository.save(new ProjectSkill(project, skill));
        }

        List<Interest> interestList = interestRepository.findAllById(interests);
        for (Interest interest : interestList) {
            projectInterestRepository.save(new ProjectInterest(project, interest));
        }
    }

    public List<Project> getList() {
        return projectRepository.findAll();
    }
}
