package vn.edu.fpt.hsf302_group5.service.impl.savedjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.hsf302_group5.entity.SavedJob;
import vn.edu.fpt.hsf302_group5.entity.SavedJobId;
import vn.edu.fpt.hsf302_group5.repository.savedjob.SavedJobRepository;
import vn.edu.fpt.hsf302_group5.service.savedjob.SavedJobService;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedJobServiceImpl implements SavedJobService {

    private final SavedJobRepository savedJobRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SavedJob> getSavedJobsByCandidateId(Integer candidateId) {
        return savedJobRepository.findByIdCandidateId(candidateId);
    }

    @Override
    @Transactional
    public void unsaveJob(Integer candidateId, Integer jobId) {
        savedJobRepository.deleteById(new SavedJobId(candidateId, jobId));
    }
}
