package vn.edu.fpt.hsf302_group5.service.email;

public interface EmailService {
    void sendVerificationEmail(String toMail, String verificationTokenLink, String messageTital);
}
