package vn.edu.fpt.hsf302_group5.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.fpt.hsf302_group5.service.user.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @GetMapping({"", "/", "/dashboard"})
    public String viewDashboard(Model model) {
        // Lấy và đưa từng dữ liệu vào Model
        model.addAttribute("totalCandidates", adminService.countCandidates());
        model.addAttribute("totalRecruiters", adminService.countRecruiters());
        model.addAttribute("totalCompanies", adminService.countCompanies());
        model.addAttribute("totalJobPosts", adminService.countJobPosts());
        model.addAttribute("recentPendingJobs", adminService.getRecentPendingJobs());
        model.addAttribute("recentCompanies", adminService.getRecentCompanies());
        
        // Trả về view templates/pages/admin/dashboard.html
        return "pages/admin/dashboard";
    }
}
