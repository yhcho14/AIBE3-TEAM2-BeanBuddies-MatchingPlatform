package com.back.domain.project.project.entity;

import com.back.domain.client.client.entity.Client;
import com.back.domain.project.project.constant.ProjectStatus;
import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectSkill> projectSkills = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectInterest> projectInterests = new ArrayList<>();


    public Project(
            Client client,
            String title,
            String summary,
            BigDecimal price,
            String preferredCondition,
            String payCondition,
            String workingCondition,
            String duration,
            String description,
            LocalDateTime deadline) {
        this.client = client;
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
