package vn.edu.fpt.hsf302_group5.service.user;

import vn.edu.fpt.hsf302_group5.dto.user.RecruiterRegisterRequest;
import vn.edu.fpt.hsf302_group5.dto.user.UserRequest;
import vn.edu.fpt.hsf302_group5.entity.User;

public interface UserService {

    Boolean registerUser(UserRequest user);

    void resendVerificationToken(String email, Boolean forgotpassword);

    void save(User user);

    void saveRecruiter(RecruiterRegisterRequest recruiterRegisterRequest);

    boolean existsByEmail(String email);

    void forgotPassword(String email);

    void resetPassword(String token, String password, String confirmPassword);
}
