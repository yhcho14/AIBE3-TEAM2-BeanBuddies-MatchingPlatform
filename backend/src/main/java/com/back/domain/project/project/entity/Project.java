package com.back.domain.project.project.entity;

import com.back.domain.project.project.constant.ProjectStatus;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Project extends BaseEntity {
    @Column(unique = true)
    private String title;
    private String summary;
    private String duration;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;
    private LocalDateTime deadline;
    private String description;
    private String preferredCondition;
    private String payCondition;
    private String workingCondition;
    // 기술, 관심 분야의 경우 다른 entity로 생성하여 관리 예정


    public Project(
            String title,
            String summary,
            BigDecimal price,
            String preferredCondition,
            String payCondition,
            String workingCondition,
            String duration,
            String description,
            LocalDateTime deadline) {
        this.title = title;
        this.summary = summary;
        this.price = price;
        this.preferredCondition = preferredCondition;
        this.payCondition = payCondition;
        this.workingCondition = workingCondition;
        this.duration = duration;
        this.description = description;
        this.deadline = deadline;
        this.status = ProjectStatus.OPEN;
    }

    public void modify(
            String title,
            String summary,
            BigDecimal price,
            String preferredCondition,
            String payCondition,
            String workingCondition,
            String duration,
            String description,
            LocalDateTime deadline,
            ProjectStatus status) {
        this.title = title;
        this.summary = summary;
        this.price = price;
        this.preferredCondition = preferredCondition;
        this.payCondition = payCondition;
        this.workingCondition = workingCondition;
        this.duration = duration;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
    }
}
