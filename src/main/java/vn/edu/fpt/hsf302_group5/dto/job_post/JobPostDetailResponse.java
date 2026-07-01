package vn.edu.fpt.hsf302_group5.dto.job_post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.fpt.hsf302_group5.entity.AdministrativeUnit;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.enums.EmploymentType;
import vn.edu.fpt.hsf302_group5.entity.enums.ExperienceLevel;
import vn.edu.fpt.hsf302_group5.entity.enums.JobLevel;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class JobPostDetailResponse {

    private Integer id;
    private String companyUrl;
    private JobLevel jobLevel;
    private ExperienceLevel experienceLevel;
    private String jobtitle;
    private String description;
    private String requirement;
    private String benefit;
    private String locationDetail;
    private String companyName;
    private String companyProvinceAddress;
    private BigDecimal  salaryMin;
    private BigDecimal salaryMax;
    private EmploymentType employmentType;
    private JobStatus status;
    private LocalDateTime postedDate;
    private LocalDateTime expiredDate;
    private String administrativeUnitName;

    public JobPostDetailResponse(Integer id, JobLevel jobLevel, ExperienceLevel experienceLevel, String companyUrl, String jobtitle, String description, String requirement, String benefit, String locationDetail, String companyName, String companyProvinceAddress, BigDecimal salaryMin, BigDecimal salaryMax, EmploymentType employmentType, JobStatus status, LocalDateTime postedDate, LocalDateTime expiredDate, String administrativeUnitName) {
        this.id = id;
        this.jobLevel = jobLevel;
        this.experienceLevel = experienceLevel;
        this.companyUrl = companyUrl;
        this.jobtitle = jobtitle;
        this.description = description;
        this.requirement = requirement;
        this.benefit = benefit;
        this.locationDetail = locationDetail;
        this.companyName = companyName;
        this.companyProvinceAddress = companyProvinceAddress;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.employmentType = employmentType;
        this.status = status;
        this.postedDate = postedDate;
        this.expiredDate = expiredDate;
        this.administrativeUnitName = administrativeUnitName;
    }
    public String getSalaryDisplay() {
        if (salaryMin == null && salaryMax == null) {
            return "Thỏa thuận";
        }

        if (salaryMin != null && salaryMax != null) {
            return salaryMin.divide(BigDecimal.valueOf(1_000_000)).intValue()
                    + " - "
                    + salaryMax.divide(BigDecimal.valueOf(1_000_000)).intValue()
                    + " triệu";
        }

        return "Thỏa thuận";
    }
}
