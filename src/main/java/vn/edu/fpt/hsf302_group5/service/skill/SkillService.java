package vn.edu.fpt.hsf302_group5.service.skill;

import org.springframework.stereotype.Service;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.SkillResponse;

import java.util.List;

public interface SkillService {
    List<SkillResponse> getAllJobSkill();
}
