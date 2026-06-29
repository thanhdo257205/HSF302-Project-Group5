package vn.edu.fpt.hsf302_group5.dto.admin;

import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDashboardResponse {
    private Long totalCandidates;
    private Long totalRecruiters;
    private Long totalCompanies;
    private Long totalJobPosts;

    private List<JobPostDashboardResponse> recentPendingJobs;
    private List<CompanyDashboardResponse> recentCompanies;
}
