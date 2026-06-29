package vn.edu.fpt.hsf302_group5.dto.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CompanyDashboardResponse {
    private Integer companyId;
    private String companyName;
    private String logoUrl;
    private String status;
    private LocalDateTime createdAt;

    // Constructor phục vụ cho JPQL Projection
    public CompanyDashboardResponse(Integer companyId, String companyName, String logoUrl, vn.edu.fpt.hsf302_group5.entity.enums.CompanyStatus status, LocalDateTime createdAt) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.logoUrl = logoUrl != null ? logoUrl : "";
        this.status = status != null ? status.name() : "";
        this.createdAt = createdAt;
    }
}
