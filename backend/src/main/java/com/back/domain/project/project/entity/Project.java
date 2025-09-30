package com.back.domain.project.project.entity;

import com.back.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
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
    private String title;
    private String summary;
    private String duration;
    private BigDecimal price;
    // private ProjectStatus status;
    private LocalDateTime startTime;
    private String description;
    private String preferredCondition;
    private String payCondition;
    private String workingCondition;
}
