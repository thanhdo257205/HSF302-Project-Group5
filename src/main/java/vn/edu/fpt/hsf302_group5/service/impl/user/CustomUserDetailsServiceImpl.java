package vn.edu.fpt.hsf302_group5.service.impl.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.hsf302_group5.dto.user.CustomUserDetailsResponse;
import vn.edu.fpt.hsf302_group5.entity.Permission;
import vn.edu.fpt.hsf302_group5.entity.User;
import vn.edu.fpt.hsf302_group5.entity.enums.UserStatus;
import vn.edu.fpt.hsf302_group5.repository.user.UserRepository;
import vn.edu.fpt.hsf302_group5.service.user.CustomUserDetailsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserRepository userRepository;

    //Spring Security gọi hàm này để lấy thông tin người dùng từ database khi thực hiện đăng nhập.
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với email " + username)); //ném về UsernameNotFoundException để có thể quay lại trang login báo rằng không có user có email này

        Set<Permission> permissions = user.getRole().getPermissions();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Permission permission : permissions) {
            grantedAuthorities.add(new SimpleGrantedAuthority(permission.getPermissionName()));
        }
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName()));

//        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
//                .password(user.getPasswordHash())
//                .disabled(user.getStatus() == UserStatus.INACTIVE)
//                .authorities(grantedAuthorities)
//                .build();

        // Nếu đăng nhập thành công sẽ tạo
        // UsernamePasswordAuthenticationToken authentication =
        //    new UsernamePasswordAuthenticationToken(
        //        customUserDetails,
        //        null,
        //        customUserDetails.getAuthorities()
        //    );
        // và lưu vào:
        // SecurityContextHolder.getContext()
        //        .setAuthentication(authentication);

        return CustomUserDetailsResponse.builder()
                .status(user.getStatus())
                .id(user.getUserId())
                .authorities(grantedAuthorities)
                .fullName(user.getFullName())
                .password(user.getPasswordHash())
                .email(user.getEmail())
                .gender(user.getGender())
                .avatarUrl(user.getAvatarUrl())
                .phone(user.getPhone())
                .build();
    }
}
