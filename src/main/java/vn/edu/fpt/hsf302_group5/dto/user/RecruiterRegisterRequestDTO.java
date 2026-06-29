package vn.edu.fpt.hsf302_group5.dto.user;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import vn.edu.fpt.hsf302_group5.entity.enums.Gender;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruiterRegisterRequestDTO {

    @NotBlank(message = "Không được để trống!")
    @Email(message = "Phải đúng định dạng của Email!")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống!")
    @Size(min = 6, max = 255, message = "Mật khẩu phải từ 6 đến 255 ký tự!")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&*()!]).+$", message = "Mật khẩu phải bao gồm cả chữ thường, chữ hoa, chữ só và kí tự đặc biệt!")
    private String password;
    private String confirmPassword;

    @NotBlank(message = "Họ tên không được để trống!")
    private String fullName;

    @NotNull(message = "Giới tính không được để trống!")
    private Gender gender;

    @NotBlank(message = "Điện thoại không được để trống!")
    @Pattern(regexp = "^(03|05|07|08|09)[0-9]{8}$", message = "Điện thoại phải đúng định dạng!")
    private String phoneNumber;

    @NotNull(message = "Tên công ty không được để trống!")
    private String companyName;

    @NotNull(message = "Tên tỉnh không được để trống!")
    private Integer provinceId;
    @NotNull(message = "Tên phường/xã không được để trống!")

    private Integer administratorUnitId;

    @NotBlank(message = "Địa chỉ cụ thể không được để trống!")
    private String addressSpecific;

    private String website;
    private String logoUrl;

}
