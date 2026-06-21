package vn.edu.fpt.hsf302_group5.dto.province;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class ProvinceResponse {

    private Integer provinceId;

    private String provinceName;

    private String provinceCode;

    public ProvinceResponse(Integer provinceId, String provinceName, String provinceCode) {
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.provinceCode = provinceCode;
    }
}
