package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.fpt.hsf302_group5.entity.enums.EmploymentType;
import vn.edu.fpt.hsf302_group5.entity.enums.ExperienceLevel;
import vn.edu.fpt.hsf302_group5.entity.enums.JobLevel;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "job_post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "recruiter_id", nullable = false)
    private Integer recruiterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", insertable = false, updatable = false)
    private Recruiter recruiter;

    @Column(name = "industry_id", nullable = false)
    private Integer industryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_id", insertable = false, updatable = false)
    private Industry industry;

    @Column(name = "approved_by")
    private Integer approvedBy;

    @Column(name = "experience_level")
    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by", insertable = false, updatable = false)
    private User approver;

    @Column(name = "job_level", length = 30)
    @Enumerated(EnumType.STRING)
    private JobLevel jobLevel;

    @Column(name = "vacancies")
    @Builder.Default
    private Integer vacancies = 1;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "requirement", columnDefinition = "NVARCHAR(MAX)")
    private String requirement;

    @Column(name = "benefit", columnDefinition = "NVARCHAR(MAX)")
    private String benefit;

    @Column(name = "location_detail", length = 200)
    private String locationDetail;

    @Column(name = "province_id")
    private Integer provinceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", insertable = false, updatable = false)
    private Province province;

    @Column(name = "administrative_unit_id")
    private Integer administrativeUnitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrative_unit_id", insertable = false, updatable = false)
    private AdministrativeUnit administrativeUnit;

    @Column(name = "salary_min", precision = 18, scale = 2)
    private BigDecimal salaryMin;

    @Column(name = "salary_max", precision = 18, scale = 2)
    private BigDecimal salaryMax;

    @Column(name = "employment_type", length = 30)
    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private JobStatus status = JobStatus.PENDING;

    @Column(name = "posted_date", nullable = false)
    @Builder.Default
    private LocalDateTime postedDate = LocalDateTime.now();

    @Column(name = "expired_date")
    private LocalDateTime expiredDate;

    @Column(name = "approved_date")
    private LocalDateTime approvedDate;

    @Column(name = "admin_comment", length = 500)
    private String adminComment;

    // Relationships
    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<SavedJob> savedJobs = new HashSet<>();

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Application> applications = new HashSet<>();
}

