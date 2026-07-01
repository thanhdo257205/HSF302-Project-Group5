package vn.edu.fpt.hsf302_group5.service.impl.user;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.hsf302_group5.dto.admin.CompanyDashboardResponse;
import vn.edu.fpt.hsf302_group5.dto.admin.JobPostDashboardResponse;
import vn.edu.fpt.hsf302_group5.entity.Company;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;
import vn.edu.fpt.hsf302_group5.entity.enums.UserRole;
import vn.edu.fpt.hsf302_group5.repository.company.CompanyRepository;
import vn.edu.fpt.hsf302_group5.repository.user.UserRepository;
import vn.edu.fpt.hsf302_group5.repository.jobpost.JobPostRepository;
import vn.edu.fpt.hsf302_group5.service.user.AdminService;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.ArrayList;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JobPostRepository jobPostRepository;

    public AdminServiceImpl(UserRepository userRepository, CompanyRepository companyRepository, JobPostRepository jobPostRepository){
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.jobPostRepository = jobPostRepository;
    }

    @Override
    public long countCandidates() {
        return userRepository.countByRole_RoleName(UserRole.CANDIDATE.name());
    }

    @Override
    public long countRecruiters() {
        return userRepository.countByRole_RoleName(UserRole.RECRUITER.name());
    }

    @Override
    public long countCompanies() {
        return companyRepository.count();
    }

    @Override
    public long countJobPosts() {
        return jobPostRepository.count();
    }

    @Override
    public List<JobPostDashboardResponse> getRecentPendingJobs() {
        List<JobPost> jobPosts = jobPostRepository.findTop5ByStatusOrderByPostedDateDesc(JobStatus.PENDING);
        List<JobPostDashboardResponse> recentPendingJobs = new ArrayList<>();
        for (JobPost job : jobPosts) {
            recentPendingJobs.add(new JobPostDashboardResponse(
                    job.getJobId(),
                    job.getTitle(),
                    job.getRecruiter().getCompany().getCompanyName(),
                    job.getStatus(),
                    job.getPostedDate()
            ));
        }
        return recentPendingJobs;
    }

    @Override
    public List<CompanyDashboardResponse> getRecentCompanies() {
        List<Company> companies = companyRepository.findTop5ByOrderByCreatedAtDesc();
        List<CompanyDashboardResponse> recentCompanies = new ArrayList<>();
        for (Company comp : companies) {
            recentCompanies.add(new CompanyDashboardResponse(
                    comp.getCompanyId(),
                    comp.getCompanyName(),
                    comp.getLogoUrl(),
                    comp.getStatus(),
                    comp.getCreatedAt()
            ));
        }
        return recentCompanies;
    }

    @Override
    public Page<JobPostDashboardResponse> getJobPostForApproval(String keyword, JobStatus status, Pageable pageable) {
        if(keyword != null && keyword.trim().isEmpty()){
            keyword = null;
        }
        Page<JobPost> entityPage = jobPostRepository.findAllForApproval(status, keyword, pageable);
        return entityPage.map(job -> new JobPostDashboardResponse(
                job.getJobId(),
                job.getTitle(),
                job.getRecruiter().getCompany().getCompanyName(),
                job.getStatus(),
                job.getPostedDate()
        ));
    }

    @Override
    public long countJobPostsByStatus(JobStatus jobStatus) {
        return jobPostRepository.countByStatus(jobStatus);
    }

    @Override
    public JobPost getJobPostById(int id) {
        return jobPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("khong tim thay tin tuyen dung voi ID: "+ id));
    }

    @Override
    public void updateJobPostStatus(int id, JobStatus status, String comment) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException(" khong tim thay tin tuyen dung voi ID: "+ id));
        jobPost.setStatus(status);
        jobPost.setAdminComment(comment);
        jobPostRepository.save(jobPost);
    }
}
