package vn.edu.fpt.hsf302_group5.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "pages/public/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "pages/public/register";
    }

    @GetMapping("/register-recruiter")
    public String registerRecruiterPage() {
        return "pages/public/register-recruiter";
    }
}
