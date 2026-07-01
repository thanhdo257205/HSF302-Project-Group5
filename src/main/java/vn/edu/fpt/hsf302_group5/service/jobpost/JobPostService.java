package vn.edu.fpt.hsf302_group5.service.jobpost;

import org.springframework.data.domain.Page;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostDetailResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.request.JobPostFormRequest;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.JobPostDashboardResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.StatisticResponse;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostResponse;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;

import java.math.BigDecimal;

public interface JobPostService {
      StatisticResponse getStatistic();

      Page<JobPostResponse> getJobPostsByFilter(String searchKeyword, Integer industryId, Integer provinceId, BigDecimal minSalary, int page);

      JobPost craeteJob(JobPostFormRequest jobPostForm);

      JobPostDetailResponse getJobPostDetaiDTOByJobPostId(Integer jobPostId);

      JobPost getJobPostById(Integer jobId);

      Page<JobPostDashboardResponse> getJobPostDashboard(String textSearch, JobStatus jobStatus, int page);

//      void updateStatusJobPost(Integer jobPostId,JobStatus jobStatus);
}
