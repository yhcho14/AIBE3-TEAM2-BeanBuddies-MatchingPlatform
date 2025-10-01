package com.back.domain.freelancer.freelancer.entity;

import com.back.domain.freelancer.join.entity.FreelancerInterest;
import com.back.domain.freelancer.join.entity.FreelancerSkill;
import com.back.domain.member.member.entity.Member;
import com.back.standard.converter.CareerConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Freelancer {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String job;

    private String freelancerEmail;

    private String comment;

    @Convert(converter = CareerConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Integer> career;

    @Column(name = "rating_avg")
    //읽기전용?
    private float ratingAvg;

    @OneToMany(mappedBy = "freelancer")
    private List<FreelancerSkill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "freelancer")
    private List<FreelancerInterest> interests = new ArrayList<>();
}
