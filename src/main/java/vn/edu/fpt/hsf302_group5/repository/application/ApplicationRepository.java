package vn.edu.fpt.hsf302_group5.repository.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.hsf302_group5.entity.Application;
import vn.edu.fpt.hsf302_group5.entity.enums.ApplicationStatus;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    @Query("""
        SELECT a FROM Application a
        WHERE a.jobId = :jobId
          AND (:searchKeyword IS NULL OR LOWER(a.candidateProfile.user.fullName) LIKE LOWER(CONCAT('%', :searchKeyword, '%')) OR LOWER(a.candidateProfile.user.email) LIKE LOWER(CONCAT('%', :searchKeyword, '%')))
          AND (:status IS NULL OR a.status = :status)
    """)
    Page<Application> findApplicants(
        @Param("jobId") Integer jobId,
        @Param("searchKeyword") String searchKeyword,
        @Param("status") ApplicationStatus status,
        Pageable pageable
    );

    @Query("""
        SELECT a FROM Application a
        JOIN FETCH a.candidateProfile cp
        JOIN FETCH cp.user u
        JOIN FETCH a.jobPost j
        LEFT JOIN FETCH a.cv c
        WHERE a.applicationId = :applicationId
    """)
    java.util.Optional<Application> findByIdWithDetails(@Param("applicationId") Integer applicationId);
}
