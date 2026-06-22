package vn.edu.fpt.hsf302_group5.service.impl.jobpost;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.fpt.hsf302_group5.dto.recruiter.request.JobPostFormRequest;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.StatisticResponse;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostResponse;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.enums.EmploymentType;
import vn.edu.fpt.hsf302_group5.entity.enums.JobLevel;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;
import vn.edu.fpt.hsf302_group5.repository.jobpost.JobPostRepository;
import vn.edu.fpt.hsf302_group5.service.jobpost.JobPostService;
import vn.edu.fpt.hsf302_group5.util.AppConstants;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class JobPostServiceImpl implements JobPostService {
    private final JobPostRepository jobPostRepository;


    @Override
    public StatisticResponse getStatistic() {
        return jobPostRepository.getStatistic();
    }

    @Override
    public Page<JobPostResponse> getJobPostsByFilter(String searchKeyword, Integer industryId, Integer provinceId, BigDecimal minSalary, int page) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            searchKeyword = null;
        }
        if (industryId == null || industryId == -1) {
            industryId = null;
        }
        if (provinceId == null || provinceId == -1) {
            provinceId = null;
        }
        if (minSalary == null) {
            minSalary = null;
        }
        Pageable pageable = PageRequest.of(page, AppConstants.NUMBER_JOB_PER_PAGE);
        return jobPostRepository.getJobPostResponseByFilter(searchKeyword, industryId, provinceId, minSalary, pageable);
    }

    @Override
    public JobPost craeteJob(JobPostFormRequest jobPostForm) {
        //đợi xong login lấy id ở session
        JobPost jobPost = new JobPost();
        jobPost.setRecruiterId(2); // tạm add cứng
        jobPost.setTitle(jobPostForm.getTitle());
        jobPost.setIndustryId(jobPostForm.getIndustryId());
        jobPost.setEmploymentType(EmploymentType.valueOf(jobPostForm.getEmploymentType()));
        jobPost.setJobLevel(JobLevel.valueOf(jobPostForm.getJobLevel()));
        jobPost.setVacancies(jobPostForm.getVacancies());
        jobPost.setProvinceId(jobPostForm.getProvinceId());
        jobPost.setLocationDetail(jobPostForm.getLocationDetail());
        jobPost.setSalaryMin(jobPostForm.getSalaryMin());
        jobPost.setSalaryMax(jobPostForm.getSalaryMax());
        jobPost.setDescription(jobPostForm.getDescription());
        jobPost.setRequirement(jobPostForm.getRequirement());
        jobPost.setExpiredDate(jobPostForm.getExpiredDate().atTime(23,59,59));
        jobPost.setStatus(JobStatus.PENDING);
        //
        return jobPostRepository.save(jobPost);
    }
}
