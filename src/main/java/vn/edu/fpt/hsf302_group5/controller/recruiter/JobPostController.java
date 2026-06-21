package vn.edu.fpt.hsf302_group5.controller.recruiter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.fpt.hsf302_group5.service.jobpost.JobPostService;

/**
 * HoaNK - HE195013
 * Date: 18/6/2026
 * Description: Lấy ra job list , thêm mới job post, xem chi tiết job post
 */
@Controller
@RequestMapping("/recruiter")
@RequiredArgsConstructor
public class JobPostController {
    private final JobPostService jobPostService;
    /**
     * HoaNK - Lấy ra list job post và đưa lên view
     */
    @GetMapping("/list-job-posts")
    public String listJobPosts(Model model) {
        //
        model.addAttribute("statistic", jobPostService.getStatistic());
        //
        return "pages/recruiter/my-job-posts";
    }
}
