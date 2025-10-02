package com.back.domain.project.project.spec;

import com.back.domain.project.project.constant.ProjectStatus;
import com.back.domain.project.project.entity.Project;
import com.back.domain.project.project.entity.ProjectInterest;
import com.back.domain.project.project.entity.ProjectSkill;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProjectSpec {

    // 키워드 검색
    public static Specification<Project> hasKeyword(String keywordType, String keyword) {
        return (root, query, cb) -> {
            if ("title".equalsIgnoreCase(keywordType)) {
                return cb.like(root.get("title"), "%" + keyword + "%");
            } else if ("summary".equalsIgnoreCase(keywordType)) {
                return cb.like(root.get("summary"), "%" + keyword + "%");
            } else if ("description".equalsIgnoreCase(keywordType)) {
                return cb.like(root.get("description"), "%" + keyword + "%");
            }
            return cb.conjunction();
        };
    }

    // 모든 skillIds를 포함하는 프로젝트만
    public static Specification<Project> hasSkills(List<Long> skillIds) {
        return (root, query, cb) -> {
            if (skillIds == null || skillIds.isEmpty()) {
                return cb.conjunction();
            }
            // Project -> ProjectSkill 조인
            Join<Project, ProjectSkill> skillJoin = root.join("projectSkills", JoinType.INNER);

            // distinct 보장
            query.groupBy(root.get("id"));
            query.having(cb.equal(cb.countDistinct(skillJoin.get("skill").get("id")), skillIds.size()));

            return skillJoin.get("skill").get("id").in(skillIds);
        };
    }

    // 모든 interestIds를 포함하는 프로젝트만
    public static Specification<Project> hasInterests(List<Long> interestIds) {
        return (root, query, cb) -> {
            if (interestIds == null || interestIds.isEmpty()) {
                return cb.conjunction();
            }
            Join<Project, ProjectInterest> interestJoin = root.join("projectInterests", JoinType.INNER);

            query.groupBy(root.get("id"));
            query.having(cb.equal(cb.countDistinct(interestJoin.get("interest").get("id")), interestIds.size()));

            return interestJoin.get("interest").get("id").in(interestIds);
        };
    }

    // 상태 조건
    public static Specification<Project> hasStatus(ProjectStatus status) {
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}

