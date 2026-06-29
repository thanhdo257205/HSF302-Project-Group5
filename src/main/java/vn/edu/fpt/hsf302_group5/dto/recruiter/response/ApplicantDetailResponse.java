package vn.edu.fpt.hsf302_group5.dto.recruiter.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.hsf302_group5.entity.enums.ApplicationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantDetailResponse {
    private Integer applicationId;
    private LocalDateTime appliedDate;
    private ApplicationStatus status;
    private String coverLetter;
    private String note;

    private Integer jobId;
    private String jobTitle;

    // Candidate details
    private String fullName;
    private String email;
    private String phone;
    private String gender;
    private LocalDate dateOfBirth;
    private String addressDetail;
    private String provinceName;
    private String summary;
    private String avatarUrl;

    // Skills, education, experiences
    private List<String> skills;
    private List<EducationDto> educations;
    private List<ExperienceDto> experiences;

    // CV files
    private String cvName;
    private String cvUrl;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EducationDto {
        private String schoolName;
        private String degree;
        private String major;
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExperienceDto {
        private String companyName;
        private String position;
        private String description;
        private LocalDate startDate;
        private LocalDate endDate;
    }
}
