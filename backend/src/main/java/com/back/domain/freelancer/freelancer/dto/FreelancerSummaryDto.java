package com.back.domain.freelancer.freelancer.dto;

import com.back.domain.common.skill.dto.SkillDto;
import com.back.domain.freelancer.freelancer.entity.Freelancer;
import com.back.domain.freelancer.join.entity.FreelancerSkill;
import java.util.List;

public record FreelancerSummaryDto(
        Long id,
        String job,
        float ratingAvg,
        List<SkillDto> skills
) {
    public FreelancerSummaryDto(Freelancer freelancer) {
        this(
                freelancer.getId(),
                freelancer.getJob(),
                freelancer.getRatingAvg(),
                freelancer.getSkills().stream()
                        .map(FreelancerSkill::getSkill)
                        .map(SkillDto::new)
                        .toList()
        );
    }
}
