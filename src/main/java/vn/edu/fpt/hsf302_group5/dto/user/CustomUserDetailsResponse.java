package vn.edu.fpt.hsf302_group5.dto.user;

import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.edu.fpt.hsf302_group5.entity.enums.Gender;
import vn.edu.fpt.hsf302_group5.entity.enums.UserStatus;

import java.util.Collection;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetailsResponse implements UserDetails {

    private Integer id;
    private String fullName;
    private String password;
    private String phone;
    private UserStatus status;
    private Gender gender;
    private String avatarUrl;
    private String email;
    private List<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVE == status;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
