package vn.edu.fpt.hsf302_group5.dto.recruiter.response;

import lombok.*;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPostDashboardResponse {
    private Integer jobId;
    private String title;
    private LocalDateTime postedDate;
    private Integer vacancies;
    private JobStatus status;
}
