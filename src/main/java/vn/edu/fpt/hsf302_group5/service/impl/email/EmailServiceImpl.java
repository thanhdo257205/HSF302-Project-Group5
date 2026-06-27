package vn.edu.fpt.hsf302_group5.service.impl.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import vn.edu.fpt.hsf302_group5.service.email.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendVerificationEmail(String  toMail, String verificationTokenLink) {
        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(toMail);
        message.setSubject("Xác thực tài khoản");
        message.setText(
                "Nhấn vào link sau để kích hoạt tài khoản:\n"
                        + verificationTokenLink
        );

        mailSender.send(message);
    }
}
