package vn.edu.fpt.hsf302_group5.controller.recruiter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.ApplicantResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.ApplicantDetailResponse;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.service.application.ApplicationService;
import vn.edu.fpt.hsf302_group5.service.jobpost.JobPostService;

@Controller
@RequestMapping("/recruiter")
@RequiredArgsConstructor
public class ApplicantController {

    private final ApplicationService applicationService;
    private final JobPostService jobPostService;

    @GetMapping("/applicant-list")
    public String getApplicantList(
            @RequestParam("jobId") Integer jobId,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        // 1. Lấy thông tin tin tuyển dụng để hiển thị tiêu đề
        JobPost jobPost = jobPostService.getJobPostById(jobId);
        if (jobPost == null) {
            return "redirect:/recruiter/list-job-posts";
        }
        model.addAttribute("jobPost", jobPost);

        // 2. Lọc và lấy danh sách ứng viên được phân trang
        Page<ApplicantResponse> applicantPage = applicationService.getApplicantsByFilter(jobId, searchKeyword, status, page);

        // 3. Đưa dữ liệu vào model hiển thị ra giao diện
        model.addAttribute("applicants", applicantPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", applicantPage.getTotalPages());
        model.addAttribute("totalElements", applicantPage.getTotalElements());
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("status", status);
        model.addAttribute("jobId", jobId);

        return "pages/recruiter/applicant-list";
    }

    @GetMapping("/applicant-detail")
    public String getApplicantDetail(@RequestParam("id") Integer applicationId, Model model, RedirectAttributes redirectAttributes) {
        try {
            ApplicantDetailResponse applicant = applicationService.getApplicantDetail(applicationId);
            model.addAttribute("applicant", applicant);
            return "pages/recruiter/applicant-detail";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/recruiter/list-job-posts";
        }
    }

    @PostMapping("/applicant/update-status")
    public String updateApplicationStatus(
            @RequestParam("applicationId") Integer applicationId,
            @RequestParam("status") String status,
            RedirectAttributes redirectAttributes) {
        try {
            applicationService.updateApplicationStatus(applicationId, status);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái hồ sơ thành công!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/recruiter/applicant-detail?id=" + applicationId;
    }
}
