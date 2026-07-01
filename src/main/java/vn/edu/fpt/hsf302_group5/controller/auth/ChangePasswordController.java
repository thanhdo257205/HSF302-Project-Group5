package vn.edu.fpt.hsf302_group5.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.hsf302_group5.entity.User;
import vn.edu.fpt.hsf302_group5.entity.VerificationToken;
import vn.edu.fpt.hsf302_group5.service.user.UserService;
import vn.edu.fpt.hsf302_group5.service.verificationtoken.VerificationTokenService;

@Controller
@RequiredArgsConstructor
public class ChangePasswordController {

    private final UserService userService;
    private final VerificationTokenService verificationTokenService;

    @PostMapping("/forgot-password")
    public String forgotPasswordRequest(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        try {
            userService.forgotPassword(email);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/forgot-password";
        }
        redirectAttributes.addAttribute("email", email);
        redirectAttributes.addFlashAttribute("forgotPassword", true);
        return "redirect:/register-success";
    }

    @GetMapping("/verify-reset-password")
    public String verifyResetPassword(@RequestParam("token") String token, RedirectAttributes redirectAttributes, Model model) {
        try {
            verificationTokenService.findByToken(token);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/forgot-password";
        }
        model.addAttribute("token",  token);
        return  "pages/public/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, RedirectAttributes redirectAttributes, Model model, @RequestParam("password") String password, @RequestParam("confirmPassword")  String confirmPassword) {
        try {
            userService.resetPassword(token, password, confirmPassword);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reset-password";
        }
        redirectAttributes.addFlashAttribute("success", "Đổi mật khẩu thành công, vui lòng đăng nhập!");
        return "redirect:/login";
    }

}
