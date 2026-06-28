package vn.edu.fpt.hsf302_group5.service.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.hsf302_group5.dto.user.UserRequertDTO;
import vn.edu.fpt.hsf302_group5.entity.User;
import vn.edu.fpt.hsf302_group5.entity.VerificationToken;
import vn.edu.fpt.hsf302_group5.entity.enums.UserRole;
import vn.edu.fpt.hsf302_group5.entity.enums.UserStatus;
import vn.edu.fpt.hsf302_group5.mapper.UserMapper;
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
}
