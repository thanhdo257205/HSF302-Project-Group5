package vn.edu.fpt.hsf302_group5.dto.recruiter.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.SkillResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPostFormRequest {
    @NotBlank(message = "Vui lòng nhập đầy đủ thông tin tiêu đề!")
    private String title;

    @NotNull(message = "Vui lòng chọn ngành nghề!")
    private Integer industryId;

    @NotBlank(message = "Vui lòng chọn cấp bậc!")
    private String jobLevel;

    @NotNull(message = "Vui lòng nhập số lượng tuyển dụng!")
    @Min(value=0, message = "Vui lòng nhập số lượng tuyển dụng > 0!")
    private Integer vacancies;

    @NotNull(message = "Vui lòng chọn ít nhất 1 kĩ năng!")
    private List<Integer> skillsId;

    @NotBlank(message = "Vui lòng nhập mô tả công việc!")
    private String description;

    @NotBlank(message = "Vui lòng nhập yêu cầu công việc!")
    private String requirement;

    @NotBlank(message = "Vui lòng nhập chi tiết địa chỉ!")
    private String locationDetail;

    @NotNull(message = "Vui lòng chọn địa điểm công ty!")
    private Integer provinceId;

    @NotNull(message = "Vui lòng chọn quận/huyện!")
    private Integer administrativeUnitId;

    @Min(value=0, message = "Vui lòng nhập mức lương > 0!")
    private BigDecimal salaryMin;

    @Min(value=0, message = "Vui lòng nhập mức lương > 0!")
    private BigDecimal salaryMax;

    @NotBlank(message = "Vui lòng chọn loại hình làm việc!")
    private String employmentType;

    @NotNull(message = "Vui lòng chọn hạn nộp hồ sơ!")
    @Future(message = "Vui lòng nhập thời gian > thời gian hiện tại!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiredDate;
}
