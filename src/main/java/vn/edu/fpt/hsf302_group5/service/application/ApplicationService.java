package vn.edu.fpt.hsf302_group5.service.application;

import org.springframework.data.domain.Page;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.ApplicantResponse;

import vn.edu.fpt.hsf302_group5.dto.recruiter.response.ApplicantDetailResponse;

public interface ApplicationService {
    Page<ApplicantResponse> getApplicantsByFilter(Integer jobId, String searchKeyword, String status, int page);
    ApplicantDetailResponse getApplicantDetail(Integer applicationId);
    void updateApplicationStatus(Integer applicationId, String status);
}
