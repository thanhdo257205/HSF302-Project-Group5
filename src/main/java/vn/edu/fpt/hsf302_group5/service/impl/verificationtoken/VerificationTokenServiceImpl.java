package vn.edu.fpt.hsf302_group5.service.impl.verificationtoken;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.fpt.hsf302_group5.dto.user.UserRequertDTO;
import vn.edu.fpt.hsf302_group5.entity.User;
import vn.edu.fpt.hsf302_group5.entity.VerificationToken;
import vn.edu.fpt.hsf302_group5.entity.enums.UserStatus;
import vn.edu.fpt.hsf302_group5.mapper.UserMapper;
import vn.edu.fpt.hsf302_group5.repository.user.UserRepository;
import vn.edu.fpt.hsf302_group5.repository.user.VerificationTokenRepository;
import vn.edu.fpt.hsf302_group5.service.verificationtoken.VerificationTokenService;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void save(VerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Transactional
    @Override
    public String verifyToken(String token) {
        VerificationToken verificationToken = findByToken(token).orElseThrow(() -> new RuntimeException("TokenNotFound"));
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("TokenExpired");
        }
        User user = verificationToken.getUser();
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        user.setVerificationToken(null);
        return user.getEmail();
    }
}
