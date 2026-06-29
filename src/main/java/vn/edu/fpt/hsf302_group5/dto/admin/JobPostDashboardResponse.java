package vn.edu.fpt.hsf302_group5.dto.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class JobPostDashboardResponse {
    private Integer jobId;
    private String title;
    private String companyName;
    private String status;
    private LocalDateTime postedDate;

    // Constructor phục vụ cho JPQL Projection
    public JobPostDashboardResponse(Integer jobId, String title, String companyName, vn.edu.fpt.hsf302_group5.entity.enums.JobStatus status, LocalDateTime postedDate) {
        this.jobId = jobId;
        this.title = title;
        this.companyName = companyName != null ? companyName : "N/A";
        this.status = status != null ? status.name() : "";
        this.postedDate = postedDate;
    }
}
