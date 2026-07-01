package vn.edu.fpt.hsf302_group5.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "pages/public/login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage(Model model) {
        return "pages/public/forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(Model model, @RequestParam(value = "token", required = false) String token) {
        model.addAttribute("token", token);
        return "pages/public/reset-password";
    }

}
