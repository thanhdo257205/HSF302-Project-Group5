package vn.edu.fpt.hsf302_group5.controller.candidate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostDetailDTO;
import vn.edu.fpt.hsf302_group5.service.jobpost.JobPostService;

@Controller
@RequestMapping("/candidate")
@RequiredArgsConstructor
public class JobDetailController {

    private final JobPostService jobPostService;

    @GetMapping("/jobs/job-detail/{id}")
    public String jobDetail(@PathVariable Integer id, Model model) {
        JobPostDetailDTO jobPostDetailDTO = jobPostService.getJobPostDetaiDTOByJobPostId(id);
        model.addAttribute("jobPostDetailDTO", jobPostDetailDTO);
        return "pages/candidate/job-detail";
    }
}
