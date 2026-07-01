package vn.edu.fpt.hsf302_group5.service.impl.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.edu.fpt.hsf302_group5.service.email.EmailService;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async("mailExecutor")
    public void sendVerificationEmail(String toMail, String verificationTokenLink, String messageTitle) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toMail);
        message.setSubject("Xác thực tài khoản");
        message.setText(
                messageTitle + "\n"
                        + verificationTokenLink
        );
        mailSender.send(message);
        log.info("Gửi email xác thực thành công tới: {}", toMail);
    }
}
