package com.back.domain.project.project.entity;

import com.back.domain.common.skill.entity.Skill;
import com.back.domain.project.project.entity.embeddedId.ProjectSkillId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class ProjectSkill {
    @EmbeddedId
    private ProjectSkillId projectSkillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId") // ProjectInterestId.projectId와 매핑
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("skillId") // ProjectInterestId.interestId와 매핑
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @CreatedDate
    private LocalDateTime createDate;

    public ProjectSkill(Project project, Skill skill) {
        this.project = project;
        this.skill = skill;
        this.projectSkillId = new ProjectSkillId(project.getId(), skill.getId());
    }
}
