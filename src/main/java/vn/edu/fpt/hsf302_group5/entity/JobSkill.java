package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSkill {

    @EmbeddedId // Dùng cái này chứ không phải @Embeddable nhé bạn!
    private JobSkillId id = new JobSkillId();

    @ManyToOne
    @MapsId("jobId") // Map chuẩn vào biến jobId trong JobSkillId
    @JoinColumn(name = "job_id")
    private JobPost jobPost;

    @ManyToOne
    @MapsId("skillId") // Map chuẩn vào biến skillId trong JobSkillId
    @JoinColumn(name = "skill_id")
    private Skill skill;
}