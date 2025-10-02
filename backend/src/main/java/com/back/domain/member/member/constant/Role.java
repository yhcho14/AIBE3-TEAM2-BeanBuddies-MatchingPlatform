package com.back.domain.member.member.constant;

import org.springframework.util.StringUtils;

public enum Role {
    CLIENT("Client"),
    FREELANCER("Freelancer"),
    ADMIN("Admin");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public static boolean isFreelancer(String description) {
        return Role.FREELANCER.description.equalsIgnoreCase(description);
    }
}
