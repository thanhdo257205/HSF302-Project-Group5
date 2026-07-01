package vn.edu.fpt.hsf302_group5.service.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.hsf302_group5.dto.user.RecruiterRegisterRequest;
import vn.edu.fpt.hsf302_group5.dto.user.UserRequest;
import vn.edu.fpt.hsf302_group5.entity.Company;
import vn.edu.fpt.hsf302_group5.entity.Recruiter;
import vn.edu.fpt.hsf302_group5.entity.User;
import vn.edu.fpt.hsf302_group5.entity.VerificationToken;
import vn.edu.fpt.hsf302_group5.entity.enums.CompanyStatus;
import vn.edu.fpt.hsf302_group5.entity.enums.UserRole;
import vn.edu.fpt.hsf302_group5.entity.enums.UserStatus;
import vn.edu.fpt.hsf302_group5.mapper.UserMapper;
import vn.edu.fpt.hsf302_group5.repository.company.CompanyRepository;
import vn.edu.fpt.hsf302_group5.repository.recruiter.RecruiterRepository;
import vn.edu.fpt.hsf302_group5.repository.role.RoleRepository;
import vn.edu.fpt.hsf302_group5.repository.user.UserRepository;
import vn.edu.fpt.hsf302_group5.service.email.EmailService;
import vn.edu.fpt.hsf302_group5.service.user.UserService;
import vn.edu.fpt.hsf302_group5.service.verificationtoken.VerificationTokenService;
import vn.edu.fpt.hsf302_group5.util.AppConstants;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final VerificationTokenService verificationTokenService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final CompanyRepository companyRepository;
    private final RecruiterRepository recruiterRepository;

    @Transactional
    @Override
    public Boolean registerUser(UserRequest user) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu không khớp!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại trên hệ thống!");
        }

        User newUser = userMapper.toUserEntity(user);
        newUser.setStatus(UserStatus.INACTIVE); // xác thực mail xong mới cho dùng
        newUser.setPasswordHash(passwordEncoder.encode(newUser.getPasswordHash()));
        newUser.setRole(roleRepository.findByRoleName(UserRole.CANDIDATE.name()));

        userRepository.save(newUser); // Hibernate đang quản lý chính object newUser trong Persistence Context và cập nhật thuộc tính id của object đó.

        String token = UUID.randomUUID().toString();
        LocalDateTime expireDate = LocalDateTime.now().plusHours(AppConstants.VERRIFI_TOKEN_REGISTER);
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(expireDate);
        verificationToken.setUser(newUser);

        verificationTokenService.save(verificationToken);
        newUser.setVerificationToken(verificationToken);

        String linkRegisterConfirm = AppConstants.LINK_VERIFY_ACCOUNT + token;

        emailService.sendVerificationEmail(newUser.getEmail(), linkRegisterConfirm, AppConstants.MESSAGE_VERYFI_ACCOUNT);

        return true;
    }

    @Transactional
    @Override
    public void resendVerificationToken(String email, Boolean forgotpassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại!"));

        String token = UUID.randomUUID().toString();
        LocalDateTime expireDate = LocalDateTime.now().plusHours(AppConstants.VERRIFI_TOKEN_REGISTER);

        VerificationToken verificationToken = user.getVerificationToken();
        if (verificationToken == null) {
            verificationToken = new VerificationToken();
            verificationToken.setUser(user);
        }
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(expireDate);

        verificationTokenService.save(verificationToken);
        user.setVerificationToken(verificationToken);
        userRepository.save(user);

        String linkRegisterConfirm = forgotpassword == false ? AppConstants.LINK_VERIFY_ACCOUNT + token : AppConstants.LINK_RESET_PASSWORD + token;
        emailService.sendVerificationEmail(user.getEmail(), linkRegisterConfirm, AppConstants.MESSAGE_VERYFI_ACCOUNT);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void saveRecruiter(RecruiterRegisterRequest dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email đã tồn tại trên hệ thống!");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu phải khớp!");
        }

        // Tạo và lưu Company
        Company company = Company.builder()
                .companyName(dto.getCompanyName())
                .website(dto.getWebsite().isBlank() ? null : dto.getWebsite())
                .logoUrl(dto.getLogoUrl().isBlank() ? null : dto.getLogoUrl())
                .provinceId(dto.getProvinceId())
                .administrativeUnitId(dto.getAdministratorUnitId())
                .addressDetail(dto.getAddressSpecific())
                .status(CompanyStatus.ACTIVE)
                .build();
        company = companyRepository.save(company);

        // Tạo và lưu User
        User user = User.builder()
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .phone(dto.getPhoneNumber())
                .gender(dto.getGender())
                .role(roleRepository.findByRoleName(UserRole.RECRUITER.name()))
                .status(UserStatus.INACTIVE)
                .build();
        user = userRepository.save(user);

        // Tạo và lưu Recruiter
        Recruiter recruiter = Recruiter.builder()
                .recruiterId(user.getUserId())
                .companyId(company.getCompanyId())
                .build();
        recruiterRepository.save(recruiter);

        // Tạo và gửi Verification Token để xác thực email
        String token = UUID.randomUUID().toString();
        LocalDateTime expireDate = LocalDateTime.now().plusHours(AppConstants.VERRIFI_TOKEN_REGISTER);
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setExpiryDate(expireDate);
        verificationToken.setUser(user);

        verificationTokenService.save(verificationToken);
        user.setVerificationToken(verificationToken);

        String linkRegisterConfirm = AppConstants.LINK_VERIFY_ACCOUNT + token;
        emailService.sendVerificationEmail(user.getEmail(), linkRegisterConfirm, AppConstants.MESSAGE_VERYFI_ACCOUNT);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    @Override
    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email không tồn tại trên hệ thống!"));
        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new RuntimeException("Tài khoản này đang bị khóa hoặc chưa được kích hoạt!");
        }
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(AppConstants.VERRIFI_TOKEN_REGISTER));
        user.setVerificationToken(verificationToken);
        verificationTokenService.save(verificationToken);
        String linkResetPassword = AppConstants.LINK_RESET_PASSWORD + token;
        emailService.sendVerificationEmail(email, linkResetPassword, AppConstants.MESSAGE_FORGOT_PASSWORD);
    }

    @Transactional
    @Override
    public void resetPassword(String token, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new RuntimeException("Mật khảu phải khớp!");
        }
        if (!password.matches("^(?=.*(\\d))(?=.*[a-z])(?=.*[!#$@$%^&*()_])(?=.*[A-Z]).+$")) {
            throw new RuntimeException("Mật khẩu phải chứa cả chữ hoa, chữ thường, chữ số và cả kí tự đặc biệt!");
        }
        User user = userRepository.findByToken(token);
        if (user == null) {
            throw new RuntimeException("Không tìm thấy người dùng!");
        }
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setVerificationToken(null);
        userRepository.save(user);
    }
}
