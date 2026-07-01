package vn.edu.fpt.hsf302_group5.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.fpt.hsf302_group5.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    //dem so luong user theo vai tro
    long countByRole_RoleName(String roleName);

    @Query("""
        select u from User u where u.verificationToken.token = :token
""")
    User findByToken(@Param("token") String token);
}
