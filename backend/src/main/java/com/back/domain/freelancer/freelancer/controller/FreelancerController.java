package com.back.domain.freelancer.freelancer.controller;

import com.back.domain.freelancer.freelancer.dto.FreelancerFilterDto;
import com.back.domain.freelancer.freelancer.dto.FreelancerSummaryDto;
import com.back.domain.freelancer.freelancer.dto.FreelancerUpdateFormDto;
import com.back.domain.freelancer.freelancer.entity.Freelancer;
import com.back.domain.freelancer.freelancer.service.FreelancerService;
import com.back.global.response.ApiResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/freelancers")
public class FreelancerController {

    private final FreelancerService freelancerService;

    @PutMapping
    public ApiResponse<FreelancerUpdateFormDto> updateFreelancer(FreelancerUpdateFormDto info) {
        freelancerService.updateFreelancer(
                info.id(),
                info.job(),
                info.freelancerEmail(),
                info.comment(),
                info.career()
        );

        return new ApiResponse<>(
                "201",
                "프리랜서 정보 변경",
                // TODO : 변경된 정보 응답
                null
        );
    }

    @GetMapping
    public ApiResponse<Page<FreelancerSummaryDto>> getFreelancers(
            FreelancerFilterDto filter,
            @PageableDefault(size = 20, sort = "ratingAvg", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<FreelancerSummaryDto> result = freelancerService.findAll(filter, pageable);

        return new ApiResponse<>(
                "200",
                "프리랜서 목록",
                result
        );
    }
}
