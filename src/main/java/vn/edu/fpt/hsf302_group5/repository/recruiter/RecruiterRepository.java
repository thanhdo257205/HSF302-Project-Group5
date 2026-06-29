package vn.edu.fpt.hsf302_group5.repository.recruiter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.hsf302_group5.entity.Recruiter;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, Integer> {
}
