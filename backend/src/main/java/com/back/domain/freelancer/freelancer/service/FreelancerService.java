package com.back.domain.freelancer.freelancer.service;

import com.back.domain.freelancer.freelancer.dto.FreelancerFilterDto;
import com.back.domain.freelancer.freelancer.dto.FreelancerSummaryDto;
import com.back.domain.freelancer.freelancer.entity.Freelancer;
import com.back.domain.freelancer.freelancer.repository.FreelancerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FreelancerService {

    private final FreelancerRepository freelancerRepository;

    @Transactional(readOnly = true)
    public Page<FreelancerSummaryDto> findAll(FreelancerFilterDto filter, Pageable page) {
        int pageNumber = page.getPageNumber();
        int pageSize = page.getPageSize();

        //TODO : 동적 쿼리 구현
        Page<Freelancer> freelancers = freelancerRepository.findAll(PageRequest.of(pageNumber, pageSize));
        return convertToSummary(freelancers);
    }

    private Page<FreelancerSummaryDto> convertToSummary(Page<Freelancer> freelancers) {
        return freelancers.map(FreelancerSummaryDto::new);
    }
}
