package vn.edu.fpt.hsf302_group5.dto.recruiter.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillResponse {
    private Integer skillId;
    private String skillName;
}
