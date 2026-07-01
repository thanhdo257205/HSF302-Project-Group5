package vn.edu.fpt.hsf302_group5.controller.recruiter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.hsf302_group5.dto.industry.IndustryResponse;
import vn.edu.fpt.hsf302_group5.dto.province.ProvinceResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.request.JobPostFormRequest;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.SkillResponse;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.enums.EmploymentType;
import vn.edu.fpt.hsf302_group5.entity.enums.JobLevel;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;
import vn.edu.fpt.hsf302_group5.service.industry.IndustryService;
import vn.edu.fpt.hsf302_group5.service.jobpost.JobPostService;
import vn.edu.fpt.hsf302_group5.service.province.ProvinceService;
import vn.edu.fpt.hsf302_group5.service.skill.SkillService;

import java.util.List;

@Controller
@RequestMapping("/recruiter")
@RequiredArgsConstructor
public class JobPostController {
    private final JobPostService jobPostService;
    private final ProvinceService provinceService;
    private final IndustryService industryService;
    private final SkillService skillService;

    @ModelAttribute("listProvince")
    public List<ProvinceResponse> getListProvince() {
        return provinceService.getListProvinceResponse();
    }

    @ModelAttribute("listIndustry")
    public List<IndustryResponse> getListIndustry() {
        return industryService.getAllIndustryResponse();
    }

    @ModelAttribute("listEnploymentType")
    public EmploymentType[] getListEmploymenType() {
        return EmploymentType.values();
    }

    @ModelAttribute("listJobLevel")
    public JobLevel[] getListJobLevel() {
        return JobLevel.values();
    }

    @ModelAttribute("skillList")
    public List<SkillResponse> getSkillList() {
        return skillService.getAllJobSkill();
    }

    @ModelAttribute("jobStatusList")
    public JobStatus[] getJobStatus() {
        return JobStatus.values();
    }

    @GetMapping("/list-job-posts")
    public String listJobPosts(Model model,
                               @RequestParam(name = "page",defaultValue = "0")int page,
                               @RequestParam(value = "text_search",required = false) String textSearch,
                               @RequestParam(value = "job_status",required = false)JobStatus jobStatus) {
        //
        model.addAttribute("jobPostDashboardList",jobPostService.getJobPostDashboard(textSearch,jobStatus,page));
        model.addAttribute("textSearch",textSearch);
        model.addAttribute("jobStatus",jobStatus);
        model.addAttribute("statistic", jobPostService.getStatistic());
        //
        return "pages/recruiter/my-job-posts";
    }


    @GetMapping("/create-job")
    public String createJob(Model model) {
        //
        model.addAttribute("jobPostForm", new JobPostFormRequest());
        //
        return "pages/recruiter/create-job";
    }

    @PostMapping("/create-job")
    public String createJob(@Valid @ModelAttribute(name="jobPostForm")JobPostFormRequest jobPostForm, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        //
        if(bindingResult.hasErrors()) {
            return "pages/recruiter/create-job";
        }
        //
        JobPost jobPost = jobPostService.craeteJob(jobPostForm);
        if(jobPost != null) {
            redirectAttributes.addFlashAttribute("addSuccess", "Tạo mới công việc thành công!");
        }else{
            redirectAttributes.addFlashAttribute("addFail", "Tạo mới công việc thất bại!");
        }
        return "redirect:/recruiter/create-job";
    }
}
