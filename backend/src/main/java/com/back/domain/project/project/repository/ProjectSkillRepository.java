package com.back.domain.project.project.repository;

import com.back.domain.project.project.entity.ProjectSkill;
import com.back.domain.project.project.entity.embeddedId.ProjectSkillId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectSkillRepository extends JpaRepository<ProjectSkill, ProjectSkillId> {
}
