package com.back.domain.project.project.entity;

import com.back.domain.common.interest.entity.Interest;
import com.back.domain.project.project.entity.embeddedId.ProjectInterestId;
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
public class ProjectInterest {
    @EmbeddedId
    private ProjectInterestId projectInterestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId") // ProjectInterestId.projectId와 매핑
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("interestId") // ProjectInterestId.interestId와 매핑
    @JoinColumn(name = "interest_id")
    private Interest interest;

    @CreatedDate
    private LocalDateTime createDate;

    public ProjectInterest(Project project, Interest interest) {
        this.project = project;
        this.interest = interest;
        this.projectInterestId = new ProjectInterestId(project.getId(), interest.getId());
    }
}
