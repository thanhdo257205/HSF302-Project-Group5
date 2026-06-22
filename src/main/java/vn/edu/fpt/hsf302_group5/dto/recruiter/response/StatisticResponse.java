package vn.edu.fpt.hsf302_group5.dto.recruiter.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticResponse {
    @NotNull(message = "Không được để trống dữ liệu thông tin tuyển dụng!")
    @Min(value=0, message = "Số lượng thông tin tuyển dụng phải >= 0")
    private Long recruitmentInformation;

    @NotNull(message = "Không được để trống dữ liệu trạng thái đang tuyển!")
    @Min(value=0, message = "Số lượng trạng thái đang tuyển dụng phải >= 0")
    private Long currentlyHiring;

    @NotNull(message = "Không được để trống dữ liệu trạng thái chờ duyệt")
    @Min(value=0, message = "Số lượng trạng thái chờ duyệt phải >= 0")
    private Long awatingApproval;

    @NotNull(message = "Không được để trống dữ liệu trạng thái từ chối!")
    @Min(value=0, message = "Số lượng trạng thái từ chối phải >= 0")
    private Long closed;
}
