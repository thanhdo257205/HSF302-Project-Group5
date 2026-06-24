package vn.edu.fpt.hsf302_group5.service.jobpost;

import org.springframework.data.domain.Page;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostDetailDTO;
import vn.edu.fpt.hsf302_group5.dto.recruiter.request.JobPostFormRequest;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.StatisticResponse;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostResponse;
import vn.edu.fpt.hsf302_group5.entity.JobPost;

import java.math.BigDecimal;

public interface JobPostService {
      StatisticResponse getStatistic();

      Page<JobPostResponse> getJobPostsByFilter(String searchKeyword, Integer industryId, Integer provinceId, BigDecimal minSalary, int page);

      JobPost craeteJob(JobPostFormRequest jobPostForm);

      JobPostDetailDTO getJobPostDetaiDTOByJobPostId(Integer jobPostId);
}
