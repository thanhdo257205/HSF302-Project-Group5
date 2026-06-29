package vn.edu.fpt.hsf302_group5.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.hsf302_group5.dto.administrativeunit.AdministrativeUnitResponse;
import vn.edu.fpt.hsf302_group5.dto.province.ProvinceResponse;
import vn.edu.fpt.hsf302_group5.dto.user.RecruiterRegisterRequestDTO;
import vn.edu.fpt.hsf302_group5.dto.user.UserRequertDTO;
import vn.edu.fpt.hsf302_group5.entity.VerificationToken;
import vn.edu.fpt.hsf302_group5.entity.enums.Gender;
import vn.edu.fpt.hsf302_group5.service.administrativeunit.AdministrativeUnitService;
import vn.edu.fpt.hsf302_group5.service.province.ProvinceService;
import vn.edu.fpt.hsf302_group5.service.user.UserService;
import vn.edu.fpt.hsf302_group5.service.verificationtoken.VerificationTokenService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;
    private final VerificationTokenService verificationTokenService;
    private final ProvinceService provinceService;
    private final AdministrativeUnitService administrativeUnitService;

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
        return "redirect:/register-success?email=" + userRequestDTO.getEmail();
    }

    @GetMapping("/register-success")
    public String registerSuccessPage(@RequestParam(value = "email", required = false) String email, @RequestParam(value = "resent", required = false) Boolean resent, Model model) {
        model.addAttribute("email", email);
        model.addAttribute("resent", resent);
        return "pages/public/register-success";
    }

    @GetMapping("/resend-verification")
    public String resendVerification(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        try {
            userService.resendVerificationToken(email);
            redirectAttributes.addAttribute("email", email);
            redirectAttributes.addAttribute("resent", true);
        } catch (Exception e) {
            redirectAttributes.addAttribute("email", email);
            redirectAttributes.addAttribute("error", e.getMessage());
        }
        return "redirect:/register-success";
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

    // Phần đăng kí tài khoản cho nhà tuyển dụng

    @GetMapping("/register-recruiter")
    public String registerRecruiterPage(Model model) {
        model.addAttribute("recruiterRegisterRequestDTO", new RecruiterRegisterRequestDTO());
        List<ProvinceResponse> provinceResponses = provinceService.getListProvinceResponse();
        model.addAttribute("genders", Gender.values());
        model.addAttribute("provinceResponses",  provinceResponses);
        return "pages/public/register-recruiter";
    }

    @GetMapping("/api/load-administrator/{id}")
    @ResponseBody
    public List<AdministrativeUnitResponse> loadAdministrator(@PathVariable("id") Integer id) {
        return administrativeUnitService.getByProvinceId(id);
    }

    @PostMapping("/register-recruiter")
    public String doRegisterRecruiter(@Valid @ModelAttribute("recruiterRegisterRequestDTO") RecruiterRegisterRequestDTO recruiterRegisterRequestDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("genders", Gender.values());
            model.addAttribute("provinceResponses", provinceService.getListProvinceResponse());
            return "pages/public/register-recruiter";
        }
        try {
            userService.saveRecruiter(recruiterRegisterRequestDTO);
        } catch (Exception e) {
            model.addAttribute("genders", Gender.values());
            model.addAttribute("provinceResponses", provinceService.getListProvinceResponse());
            model.addAttribute("errors", e.getMessage());
            return "pages/public/register-recruiter";
        }
        redirectAttributes.addAttribute("email", recruiterRegisterRequestDTO.getEmail());
        return "redirect:/register-success";
    }
}
