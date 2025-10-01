package com.back.domain.project.project.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProjectWriteReqBody(
        String title,
        String summary,
        String duration,
        BigDecimal price,
        String preferredCondition,
        String payCondition,
        String description,
        LocalDateTime deadline,
        List<Long> skills,
        List<Long> interests
) {
}

