package vn.edu.fpt.hsf302_group5.service.savedjob;

import vn.edu.fpt.hsf302_group5.entity.SavedJob;
import java.util.List;

public interface SavedJobService {
    List<SavedJob> getSavedJobsByCandidateId(Integer candidateId);
    void unsaveJob(Integer candidateId, Integer jobId);
    void saveJob(Integer candidateId, Integer jobId);
    boolean isJobSaved(Integer candidateId, Integer jobId);
}
