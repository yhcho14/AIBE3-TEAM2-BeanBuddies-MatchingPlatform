package com.back.domain.project.project.constant;

import lombok.Getter;

@Getter
public enum ProjectStatus {
    OPEN("모집중"),
    IN_PROGRESS("진행중"),
    COMPLETED("완료"),
    CLOSED("종료");

    private final String label;

    ProjectStatus(String label) {
        this.label = label;
    }

}

