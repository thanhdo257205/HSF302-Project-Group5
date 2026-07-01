package vn.edu.fpt.hsf302_group5.dto.job_post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class JobPostResponse {

    private Integer jobId;
    private String jobTitle;
    private String companyName;
    private String companyProvinceAddress;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private LocalDateTime expirationDate;
    private String companyLogoUrl;

    public JobPostResponse(Integer jobId, String jobTitle, String companyName, String companyProvinceAddress, BigDecimal salaryMin, BigDecimal salaryMax, LocalDateTime expirationDate, String companyLogoUrl) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.companyProvinceAddress = companyProvinceAddress;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.expirationDate = expirationDate;
        this.companyLogoUrl = companyLogoUrl;
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
