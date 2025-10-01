package com.back.domain.project.project.dto;

import com.back.domain.project.project.constant.ProjectStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectModifyReqBody(
        @NotBlank(message = "제목은 필수입니다.")
        String title,

        @NotBlank(message = "요약은 필수입니다.")
        String summary,

        @NotBlank(message = "기간은 필수입니다.")
        String duration,

        @NotNull(message = "가격은 필수입니다.")
        @DecimalMin(value = "0", inclusive = false, message = "가격은 0보다 커야 합니다.")
        BigDecimal price,

        @NotBlank(message = "우대 조건은 필수입니다.")
        String preferredCondition,

        @NotBlank(message = "급여 조건은 필수입니다.")
        String payCondition,

        @NotBlank(message = "업무 조건은 필수입니다.")
        String workingCondition,

        @NotBlank(message = "상세 설명은 필수입니다.")
        String description,

        @NotNull(message = "마감일은 필수입니다.")
        LocalDateTime deadline,

        ProjectStatus status,

        List<Long> skills,
        List<Long> interests
) {
}
