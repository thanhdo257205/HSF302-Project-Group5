package vn.edu.fpt.hsf302_group5.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.hsf302_group5.dto.user.UserRequertDTO;
import vn.edu.fpt.hsf302_group5.entity.VerificationToken;
import vn.edu.fpt.hsf302_group5.service.user.UserService;
import vn.edu.fpt.hsf302_group5.service.verificationtoken.VerificationTokenService;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;
    private final VerificationTokenService verificationTokenService;

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("userRequestDTO", new UserRequertDTO());
        return "pages/public/register";
    }

    @PostMapping("/register")
    public String doRegister(@Valid @ModelAttribute("userRequestDTO") UserRequertDTO userRequestDTO, BindingResult bindingResult, @RequestParam(value = "acceptTerms", required = false) String[] acceptTerms, Model model) {
        if (bindingResult.hasErrors()) {
            return "pages/public/register";
        }
        if (acceptTerms == null || acceptTerms.length == 0) {
            model.addAttribute("acceptTermsError", "Bạn phải đồng ý với các điều khoản để có thể đăng kí tài khoản!");
            return "pages/public/register";
        }
        try {
            userService.registerUser(userRequestDTO);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "pages/public/register";
        }
        return "redirect:/register-success";
    }

    @GetMapping("/register-success")
    public String registerSuccessPage() {
        return "pages/public/register-success";
    }

    @GetMapping("/verify")
    public String verifyPage(@RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        String email = null;
        String error = null;
        try {
            email = verificationTokenService.verifyToken(token);
        } catch (Exception e) {
            error = e.getMessage();
        }
        if (error == null) {
            redirectAttributes.addFlashAttribute("email", email);
        } else {
            redirectAttributes.addAttribute("error", error);
        }
        return "redirect:/login";
    }
}
