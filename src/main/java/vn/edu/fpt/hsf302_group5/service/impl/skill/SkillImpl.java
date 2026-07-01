package vn.edu.fpt.hsf302_group5.service.impl.skill;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.SkillResponse;
import vn.edu.fpt.hsf302_group5.entity.Skill;
import vn.edu.fpt.hsf302_group5.repository.skill.SkillRepository;
import vn.edu.fpt.hsf302_group5.service.skill.SkillService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillImpl implements SkillService {
    private final SkillRepository skillRepository;


    @Override
    public List<SkillResponse> getAllJobSkill() {
        List<Skill> listSkills = skillRepository.findAll();
        List<SkillResponse> listSkillsResponse = new ArrayList<>();
        for(Skill skill : listSkills) {
            listSkillsResponse.add(new SkillResponse(skill.getSkillId(),skill.getSkillName()));
        }
        return listSkillsResponse;
    }
}
