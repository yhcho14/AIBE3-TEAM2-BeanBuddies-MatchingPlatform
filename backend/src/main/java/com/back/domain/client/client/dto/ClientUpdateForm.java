package com.back.domain.client.client.dto;

public record ClientUpdateForm(
        Long id,
        String companySize,
        String companyDescription,
        String representative,
        String businessNo,
        String companyPhone,
        String companyEmail
) {
}
