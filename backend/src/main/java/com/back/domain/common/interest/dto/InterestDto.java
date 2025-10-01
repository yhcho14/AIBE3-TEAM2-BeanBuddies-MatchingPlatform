package com.back.domain.common.interest.dto;

import com.back.domain.common.interest.entity.Interest;

public record InterestDto(
        Long id,
        String name
) {
    public InterestDto (Interest interest) {
        this(
                interest.getId(),
                interest.getName()
        );
    }
}
