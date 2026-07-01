package vn.edu.fpt.hsf302_group5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import vn.edu.fpt.hsf302_group5.dto.recruiter.request.JobPostFormRequest;
import vn.edu.fpt.hsf302_group5.entity.JobPost;

@Mapper(componentModel = "spring")
public interface JobPostMapper {
    @Mapping(target="expiredDate", expression = "java(jobPostFormRequest.getExpiredDate() != null ? jobPostFormRequest.getExpiredDate().atTime(23,59,59) : null)")

    JobPost toEntity(JobPostFormRequest jobPostFormRequest);
}
