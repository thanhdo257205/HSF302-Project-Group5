package vn.edu.fpt.hsf302_group5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.ApplicantResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.ApplicantDetailResponse;
import vn.edu.fpt.hsf302_group5.entity.Application;
import vn.edu.fpt.hsf302_group5.entity.CandidateSkill;
import vn.edu.fpt.hsf302_group5.entity.Education;
import vn.edu.fpt.hsf302_group5.entity.Experience;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    @Mapping(source = "candidateProfile.user.fullName", target = "fullName")
    @Mapping(source = "candidateProfile.user.email", target = "email")
    @Mapping(source = "candidateProfile.user.phone", target = "phone")
    ApplicantResponse toApplicantResponse(Application application);

    @Mapping(source = "candidateProfile.user.fullName", target = "fullName")
    @Mapping(source = "candidateProfile.user.email", target = "email")
    @Mapping(source = "candidateProfile.user.phone", target = "phone")
    @Mapping(source = "candidateProfile.user.avatarUrl", target = "avatarUrl")
    @Mapping(source = "jobPost.jobId", target = "jobId")
    @Mapping(source = "jobPost.title", target = "jobTitle")
    @Mapping(source = "candidateProfile.dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "candidateProfile.addressDetail", target = "addressDetail")
    @Mapping(source = "candidateProfile.province.provinceName", target = "provinceName")
    @Mapping(source = "candidateProfile.summary", target = "summary")
    @Mapping(source = "candidateProfile.skills", target = "skills", qualifiedByName = "mapSkills")
    @Mapping(source = "candidateProfile.educations", target = "educations")
    @Mapping(source = "candidateProfile.experiences", target = "experiences")
    @Mapping(source = "cv.cvName", target = "cvName")
    @Mapping(source = "cv.fileUrl", target = "cvUrl")
    @Mapping(target = "gender", expression = "java(application.getCandidateProfile().getUser().getGender() != null ? application.getCandidateProfile().getUser().getGender().name() : (application.getCandidateProfile().getGender() != null ? application.getCandidateProfile().getGender().name() : null))")
    ApplicantDetailResponse toApplicantDetailResponse(Application application);

    @Named("mapSkills")
    default List<String> mapSkills(Set<CandidateSkill> skills) {
        if (skills == null) return List.of();
        return skills.stream()
                .map(cs -> cs.getSkill().getSkillName())
                .toList();
    }

    ApplicantDetailResponse.EducationDto toEducationDto(Education education);

    ApplicantDetailResponse.ExperienceDto toExperienceDto(Experience experience);
}
