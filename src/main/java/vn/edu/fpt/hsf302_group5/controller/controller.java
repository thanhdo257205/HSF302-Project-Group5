package vn.edu.fpt.hsf302_group5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class controller {

    @GetMapping("/test")
    public String test(){
        return "pages/candidate/application-detail";
    }

    @GetMapping("/test1")
    public String test1(){
        return "pages/candidate/job-detail";
    }

    @GetMapping("/test2")
    public String test2(){
        return "pages/candidate/job-list";
    }

    @GetMapping("/test3")
    public String test3(){
        return "pages/candidate/my-applications";
    }

    @GetMapping("/test4")
    public String test4(){
        return "pages/candidate/profile";
    }

    @GetMapping("/test5")
    public String test5(){
        return "pages/candidate/saved-jobs";
    }

    @GetMapping("/")
    public String test6(){
        return "pages/public/home";
    }
}
