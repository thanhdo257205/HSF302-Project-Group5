package vn.edu.fpt.hsf302_group5.controller.candidate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostDetailResponse;
import vn.edu.fpt.hsf302_group5.entity.User;
import vn.edu.fpt.hsf302_group5.repository.user.UserRepository;
import vn.edu.fpt.hsf302_group5.service.jobpost.JobPostService;
import vn.edu.fpt.hsf302_group5.service.savedjob.SavedJobService;

@Controller
@RequestMapping("/candidate")
@RequiredArgsConstructor
public class JobDetailController {

    private final JobPostService jobPostService;
    private final SavedJobService savedJobService;
    private final UserRepository userRepository;

    @GetMapping("/jobs/job-detail/{id}")
    public String jobDetail(@PathVariable Integer id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        JobPostDetailResponse jobPostDetailDTO = jobPostService.getJobPostDetaiDTOByJobPostId(id);
        model.addAttribute("jobPostDetailDTO", jobPostDetailDTO);

        boolean isSaved = false;
        if (userDetails != null) {
            User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
            if (user != null) {
                isSaved = savedJobService.isJobSaved(user.getUserId(), id);
            }
        }
        model.addAttribute("isSaved", isSaved);
        return "pages/candidate/job-detail";
    }
}
