package vn.edu.fpt.hsf302_group5.dto.industry;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IndustryResponse {

    private Integer industryId;

    private String industryName;

    public IndustryResponse(Integer industryId, String industryName) {
        this.industryId = industryId;
        this.industryName = industryName;
    }
}
