package vn.edu.fpt.hsf302_group5.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.fpt.hsf302_group5.entity.User;
import vn.edu.fpt.hsf302_group5.entity.enums.UserRole;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    //dem so luong user theo vai tro
    long countByRole(UserRole userRole);
}
