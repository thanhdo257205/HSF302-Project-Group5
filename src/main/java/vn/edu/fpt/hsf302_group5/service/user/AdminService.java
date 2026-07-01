package vn.edu.fpt.hsf302_group5.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.fpt.hsf302_group5.dto.admin.CompanyDashboardResponse;
import vn.edu.fpt.hsf302_group5.dto.admin.JobPostDashboardResponse;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;

import java.util.List;

public interface AdminService {
    long countCandidates();
    long countRecruiters();
    long countCompanies();
    long countJobPosts();
    List<JobPostDashboardResponse> getRecentPendingJobs();
    List<CompanyDashboardResponse> getRecentCompanies();

    Page<JobPostDashboardResponse> getJobPostForApproval(String keyword, JobStatus status, Pageable pageable);
    long countJobPostsByStatus(JobStatus jobStatus);
    JobPost getJobPostById(int id);
    void updateJobPostStatus(int id, JobStatus job, String comment);
}
