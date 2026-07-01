package vn.edu.fpt.hsf302_group5.controller.candidate;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.fpt.hsf302_group5.entity.SavedJob;
import vn.edu.fpt.hsf302_group5.entity.User;
import vn.edu.fpt.hsf302_group5.repository.user.UserRepository;
import vn.edu.fpt.hsf302_group5.service.savedjob.SavedJobService;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/candidate")
public class SavedJobsController {

    private final SavedJobService savedJobService;
    private final UserRepository userRepository;

    @GetMapping("/saved-jobs")
    public String savedJobs(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
        
        List<SavedJob> savedJobs = savedJobService.getSavedJobsByCandidateId(user.getUserId());
        
        model.addAttribute("savedJobs", savedJobs);
        return "pages/candidate/saved-jobs";
    }

    @org.springframework.web.bind.annotation.PostMapping("/saved-jobs/unsave")
    public String unsaveJob(@org.springframework.web.bind.annotation.RequestParam Integer jobId, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
                
        savedJobService.unsaveJob(user.getUserId(), jobId);
        return "redirect:/candidate/saved-jobs";
    }
}
