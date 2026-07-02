package vn.edu.fpt.hsf302_group5.service.impl.savedjob;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.hsf302_group5.entity.CandidateProfile;
import vn.edu.fpt.hsf302_group5.entity.SavedJob;
import vn.edu.fpt.hsf302_group5.entity.SavedJobId;
import vn.edu.fpt.hsf302_group5.entity.User;
import vn.edu.fpt.hsf302_group5.repository.savedjob.SavedJobRepository;
import vn.edu.fpt.hsf302_group5.repository.user.UserRepository;
import vn.edu.fpt.hsf302_group5.service.savedjob.SavedJobService;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedJobServiceImpl implements SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final UserRepository userRepository;

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

    @Override
    @Transactional
    public void saveJob(Integer candidateId, Integer jobId) {
        User user = userRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
        if (user.getCandidateProfile() == null) {
            CandidateProfile profile = CandidateProfile.builder()
                    .candidateId(candidateId)
                    .user(user)
                    .build();
            user.setCandidateProfile(profile);
            userRepository.save(user);
        }

        SavedJobId id = new SavedJobId(candidateId, jobId);
        if (!savedJobRepository.existsById(id)) {
            SavedJob savedJob = SavedJob.builder()
                    .id(id)
                    .savedAt(java.time.LocalDateTime.now())
                    .build();
            savedJobRepository.save(savedJob);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isJobSaved(Integer candidateId, Integer jobId) {
        return savedJobRepository.existsById(new SavedJobId(candidateId, jobId));
    }
}
