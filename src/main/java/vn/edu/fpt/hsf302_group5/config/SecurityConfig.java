package vn.edu.fpt.hsf302_group5.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.util.AntPathMatcher;

@Configuration
@EnableWebSecurity // Kích hoạt cơ chế bảo mật của Spring Security
@EnableMethodSecurity // bật phân quyền ở level phương thức với @PreAuthorize
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider( //chịu trách nhiệm thực hiện quá trình xác thực (Authentication) thông tin đăng nhập từ cơ sở dữ liệu.
            UserDetailsService userDetailsService, 
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Vô hiệu hóa CSRF tạm thời để phát triển/kiểm thử
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home/**", "/login", "/register", "/register-success", "/resend-verification", "/privacy-policy", "/register-recruiter", "/verify", "/forgot-password", "/reset-password", "/css/**", "/js/**", "/images/**", "/assets/**", "/api/load-administrator/**", "/do-login", "/verify-reset-password").permitAll() // Cho phép truy cập tài nguyên tĩnh và các trang không cần xác thực
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/do-login") // Post
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureHandler(((request, response, exception) -> {
                            if (exception instanceof DisabledException) {
                                response.sendRedirect("/login?error=accountInactive");
                            } else if (exception instanceof UsernameNotFoundException) {
                                response.sendRedirect("/login?error=userNotFound");
                            } else {
                                response.sendRedirect("/login?error=badCredentials");
                            }
                        }))
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // POST
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .build();
    }
}
