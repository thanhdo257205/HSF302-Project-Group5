package vn.edu.fpt.hsf302_group5.controller.recruiter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.hsf302_group5.dto.industry.IndustryResponse;
import vn.edu.fpt.hsf302_group5.dto.province.ProvinceResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.request.JobPostFormRequest;
import vn.edu.fpt.hsf302_group5.entity.enums.EmploymentType;
import vn.edu.fpt.hsf302_group5.entity.enums.JobLevel;
import vn.edu.fpt.hsf302_group5.service.industry.IndustryService;
import vn.edu.fpt.hsf302_group5.service.jobpost.JobPostService;
import vn.edu.fpt.hsf302_group5.service.province.ProvinceService;

import java.util.List;

@Controller
@RequestMapping("/recruiter")
@RequiredArgsConstructor
public class JobPostController {
    private final JobPostService jobPostService;
    private final ProvinceService provinceService;
    private final IndustryService industryService;

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

    @GetMapping("/list-job-posts")
    public String listJobPosts(Model model) {
        //
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
        jobPostService.craeteJob(jobPostForm);
        redirectAttributes.addFlashAttribute("addSuccess", "Tạo mới job thành công!");
        return "redirect:/recruiter/create-job";
    }
}
