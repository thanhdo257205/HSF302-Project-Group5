package vn.edu.fpt.hsf302_group5.service.jobpost;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.fpt.hsf302_group5.dto.StatisticResponse;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostResponse;

import java.math.BigDecimal;
import java.util.List;

public interface JobPostService {
    StatisticResponse getStatistic();

    public Page<JobPostResponse> getJobPostsByFilter(String searchKeyword, Integer industryId, Integer provinceId, BigDecimal minSalary, int page);
}
