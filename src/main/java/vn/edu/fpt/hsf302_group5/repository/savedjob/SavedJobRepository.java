package vn.edu.fpt.hsf302_group5.repository.savedjob;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.hsf302_group5.entity.SavedJob;
import vn.edu.fpt.hsf302_group5.entity.SavedJobId;
import java.util.List;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJob, SavedJobId> {
    List<SavedJob> findByIdCandidateId(Integer candidateId);
}
