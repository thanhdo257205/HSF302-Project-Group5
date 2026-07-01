package vn.edu.fpt.hsf302_group5.service.impl.jobpost;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostDetailResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.request.JobPostFormRequest;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.JobPostDashboardResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.SkillResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.StatisticResponse;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostResponse;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.JobSkill;
import vn.edu.fpt.hsf302_group5.entity.Skill;
import vn.edu.fpt.hsf302_group5.entity.enums.EmploymentType;
import vn.edu.fpt.hsf302_group5.entity.enums.JobLevel;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;
import vn.edu.fpt.hsf302_group5.mapper.JobPostMapper;
import vn.edu.fpt.hsf302_group5.repository.jobpost.JobPostRepository;
import vn.edu.fpt.hsf302_group5.repository.skill.SkillRepository;
import vn.edu.fpt.hsf302_group5.service.jobpost.JobPostService;
import vn.edu.fpt.hsf302_group5.util.AppConstants;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobPostServiceImpl implements JobPostService {
    private final JobPostRepository jobPostRepository;
    private final JobPostMapper jobPostMapper;
    private final SkillRepository skillRepository;

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
    @Transactional
    public JobPost craeteJob(JobPostFormRequest jobPostForm) {
        //đợi xong login lấy id ở session
        JobPost jobPost = jobPostMapper.toEntity(jobPostForm);
        jobPost.setRecruiterId(2);
        jobPost.setStatus(JobStatus.PENDING);
        //insert dữ liệu vào job_skill
        if(jobPostForm.getSkillsId() != null && !jobPostForm.getSkillsId().isEmpty()) {
            Set<JobSkill> setJobSkills = new HashSet<>();
            for(Integer skillId : jobPostForm.getSkillsId()) {
                JobSkill jobSkill = new JobSkill();
                jobSkill.setJobPost(jobPost);

                Skill skill = skillRepository.getReferenceById(skillId);
                jobSkill.setSkill(skill);

                setJobSkills.add(jobSkill);
            }
            jobPost.setJobSkills(setJobSkills);
        }
        //
        return jobPostRepository.save(jobPost);
    }

    @Override
    public JobPostDetailResponse getJobPostDetaiDTOByJobPostId(Integer jobPostId) {
        JobPostDetailResponse jobPostDetailResponse = jobPostRepository.getJobPostDetaiDTOByJobPostId(jobPostId);
        return jobPostDetailResponse;
    }

    @Override
    public JobPost getJobPostById(Integer jobId) {
        return jobPostRepository.findById(jobId).orElse(null);
    }

    @Override
    public Page<JobPostDashboardResponse> getJobPostDashboard(String textSearch, JobStatus jobStatus, int page) {
        if(textSearch == null || textSearch.isEmpty()) {
            textSearch = null;
        }
        if(jobStatus == null) {
            jobStatus = null;
        }
        Pageable pageable = PageRequest.of(page,AppConstants.NUMBER_PAGE_PER_BLOCK);

        return jobPostRepository.getJobPostDashboard(textSearch,jobStatus,pageable);
    }

//    @Override
//    @Transactional
//    public void updateStatusJobPost(Integer jobPostId, JobStatus jobStatus) {
//        // tìm ra job có id đc truyền vào
//        JobPost jobPost = jobPostRepository.findById(jobPostId)
//                .orElseThrow(() -> new IllegalArgumentException("Job không tồn tại!"));
//
//        if(JobStatus.APPROVED == jobStatus) {
//            jobPost.setStatus(JobStatus.APPROVED);
//        }else if(JobStatus.CLOSED == jobStatus) {
//            jobPost.setStatus(JobStatus.CLOSED);
//        }
//        jobPostRepository.save(jobPost);
//    }


}
