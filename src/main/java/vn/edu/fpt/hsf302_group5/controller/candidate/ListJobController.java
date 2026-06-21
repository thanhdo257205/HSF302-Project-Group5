package vn.edu.fpt.hsf302_group5.controller.candidate;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.fpt.hsf302_group5.dto.industry.IndustryResponse;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostResponse;
import vn.edu.fpt.hsf302_group5.dto.province.ProvinceResponse;
import vn.edu.fpt.hsf302_group5.service.industry.IndustryService;
import vn.edu.fpt.hsf302_group5.service.jobpost.JobPostService;
import vn.edu.fpt.hsf302_group5.service.province.ProvinceService;
import vn.edu.fpt.hsf302_group5.util.AppConstants;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/candidate")
public class ListJobController {


    private final ProvinceService provinceService;
    private final IndustryService industryService;
    private final JobPostService jobPostService;

    @GetMapping("/jobs/list-job")
    public String listJob(Model model, @RequestParam(name = "industry", required = false) Integer industryId, @RequestParam(name = "search-keyword", required = false) String search_keyword, @RequestParam(name = "province", required = false) Integer provinceId, @RequestParam(name = "minSalary", required = false) BigDecimal minSalary, @RequestParam(name = "page", defaultValue = "0") int page) {
        List<ProvinceResponse> provinceResponses = provinceService.getListProvinceResponse();
        List<IndustryResponse> industryResponses = industryService.getAllIndustryResponse();

        Page<JobPostResponse> jobPage = jobPostService.getJobPostsByFilter(search_keyword, industryId, provinceId,minSalary, page);
        int startPage = (jobPage.getNumber() / AppConstants.NUMBER_PAGE_PER_BLOCK) * AppConstants.NUMBER_PAGE_PER_BLOCK;
        int endPage = Math.min(startPage + AppConstants.NUMBER_PAGE_PER_BLOCK - 1, jobPage.getTotalPages() - 1);

        model.addAttribute("jobPage", jobPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("industryId", industryId);
        model.addAttribute("provinceId", provinceId);
        model.addAttribute("jobPostResponses", jobPage);
        model.addAttribute("minSalary", minSalary);
        model.addAttribute("searchKeyword", search_keyword);
        model.addAttribute("provinceResponses", provinceResponses);
        model.addAttribute("industryResponses", industryResponses);
        return "pages/candidate/job-list";
    }

}
