package vn.edu.fpt.hsf302_group5.repository.jobpost;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostDetailResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.JobPostDashboardResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.StatisticResponse;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostResponse;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost,Integer> {
    @Query("""
            SELECT new vn.edu.fpt.hsf302_group5.dto.recruiter.response.StatisticResponse(
            COUNT(j),
            COUNT(CASE WHEN j.status = vn.edu.fpt.hsf302_group5.entity.enums.JobStatus.APPROVED THEN  1 END),
            COUNT(CASE WHEN j.status = vn.edu.fpt.hsf302_group5.entity.enums.JobStatus.PENDING THEN 1 END),
            COUNT(CASE WHEN j.status IN (vn.edu.fpt.hsf302_group5.entity.enums.JobStatus.CLOSED, 
            vn.edu.fpt.hsf302_group5.entity.enums.JobStatus.REJECTED) THEN 1 END)
            ) 
            FROM JobPost j 
""")
    StatisticResponse getStatistic();

    @Query("""
    select new vn.edu.fpt.hsf302_group5.dto.job_post.JobPostResponse(
        j.jobId, j.title,
        j.recruiter.company.companyName,
        j.province.provinceName,
        j.salaryMin,
        j.salaryMax,
        j.expiredDate,
        j.recruiter.company.logoUrl
    )
    from JobPost j
    where (:searchKeyword is null
           or lower(j.title) like lower(concat('%', :searchKeyword, '%')))
      and (:industryId is null
           or j.industry.industryId = :industryId)
      and (:provinceId is null
           or j.province.provinceId = :provinceId)
      and (:salaryMin is null
           or j.salaryMin >= :salaryMin)
""")
    Page<JobPostResponse> getJobPostResponseByFilter(
            @Param("searchKeyword") String searchKeyword,
            @Param("industryId") Integer industryId,
            @Param("provinceId") Integer provinceId,
            @Param("salaryMin") BigDecimal salaryMin,
            Pageable pageable
    );


    @Query("""
             SELECT new vn.edu.fpt.hsf302_group5.dto.job_post.JobPostDetailResponse(
                 j.jobId, 
                 j.jobLevel, 
                 j.experienceLevel,
                 c.logoUrl, 
                 j.title, 
                 j.description, 
                 j.requirement, 
                 j.benefit, 
                 j.locationDetail, 
                 c.companyName, 
                 p.provinceName, 
                 j.salaryMin, 
                 j.salaryMax, 
                 j.employmentType, 
                 j.status, 
                 j.postedDate, 
                 j.expiredDate, 
                 au.unitName
             ) 
             FROM JobPost j 
             LEFT JOIN j.recruiter r 
             LEFT JOIN r.company c 
             LEFT JOIN j.province p 
             LEFT JOIN j.administrativeUnit au 
             WHERE j.jobId = :jobPostId
            """)
    JobPostDetailResponse getJobPostDetaiDTOByJobPostId(@Param("jobPostId") Integer jobPostId);
    List<JobPost> findTop5ByStatusOrderByPostedDateDesc(
        JobStatus status
    );

    @Query("""
        SELECT new vn.edu.fpt.hsf302_group5.dto.recruiter.response.JobPostDashboardResponse(j.title,
         j.postedDate,
         j.vacancies,
         j.status
         ) 
        FROM JobPost j 
        WHERE (:textSearch is null 
        or lower(j.title) like lower(concat('%', :textSearch, '%')))
        and (:statusSearch is null
        or j.status = :statusSearch
        )     
""")
    Page<JobPostDashboardResponse> getJobPostDashboard(
            @Param("textSearch") String textSearch,
            @Param("statusSearch") JobStatus statusSearch,
            Pageable pageable
    );
}
