package vn.edu.fpt.hsf302_group5.service.user;

import vn.edu.fpt.hsf302_group5.dto.user.UserRequertDTO;
import vn.edu.fpt.hsf302_group5.entity.User;

public interface UserService {

    Boolean registerUser(UserRequertDTO user);

    void resendVerificationToken(String email);

    void save(User user);
}
