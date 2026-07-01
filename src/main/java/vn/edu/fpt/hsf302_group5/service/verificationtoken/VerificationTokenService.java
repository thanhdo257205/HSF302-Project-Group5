package vn.edu.fpt.hsf302_group5.service.verificationtoken;

import vn.edu.fpt.hsf302_group5.entity.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {

    void save(VerificationToken verificationToken);

     VerificationToken findByToken(String token);

    String verifyToken(String token);
}
