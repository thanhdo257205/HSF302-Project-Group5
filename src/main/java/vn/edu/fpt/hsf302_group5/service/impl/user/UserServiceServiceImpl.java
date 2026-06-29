package vn.edu.fpt.hsf302_group5.service.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.hsf302_group5.dto.user.RecruiterRegisterRequestDTO;
import vn.edu.fpt.hsf302_group5.dto.user.UserRequertDTO;
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
    public Boolean registerUser(UserRequertDTO user) {
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

        emailService.sendVerificationEmail(newUser.getEmail(), linkRegisterConfirm);

        return true;
    }

    @Transactional
    @Override
    public void resendVerificationToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại!"));

        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new RuntimeException("Tài khoản đã được kích hoạt trước đó!");
        }

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

        String linkRegisterConfirm = AppConstants.LINK_VERIFY_ACCOUNT + token;
        emailService.sendVerificationEmail(user.getEmail(), linkRegisterConfirm);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void saveRecruiter(RecruiterRegisterRequestDTO dto) {
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
        emailService.sendVerificationEmail(user.getEmail(), linkRegisterConfirm);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
