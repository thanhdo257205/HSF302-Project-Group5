package vn.edu.fpt.hsf302_group5.service.impl.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import vn.edu.fpt.hsf302_group5.service.email.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String  toMail, String verificationTokenLink) {
        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setFrom(System.getProperty("MAIL_USERNAME"));
        message.setTo(toMail);
        message.setSubject("Xác thực tài khoản");
        message.setText(
                "Nhấn vào link sau để kích hoạt tài khoản:\n"
                        + verificationTokenLink
        );

        mailSender.send(message);
    }
}
