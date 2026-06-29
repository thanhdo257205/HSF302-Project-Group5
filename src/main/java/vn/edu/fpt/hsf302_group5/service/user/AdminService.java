package vn.edu.fpt.hsf302_group5.service.user;

import vn.edu.fpt.hsf302_group5.dto.admin.CompanyDashboardResponse;
import vn.edu.fpt.hsf302_group5.dto.admin.JobPostDashboardResponse;
import java.util.List;

public interface AdminService {
    long countCandidates();
    long countRecruiters();
    long countCompanies();
    long countJobPosts();
    List<JobPostDashboardResponse> getRecentPendingJobs();
    List<CompanyDashboardResponse> getRecentCompanies();
}
