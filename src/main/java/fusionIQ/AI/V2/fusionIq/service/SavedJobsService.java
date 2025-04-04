package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Job;
import fusionIQ.AI.V2.fusionIq.data.SavedJobs;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.JobRepository;
import fusionIQ.AI.V2.fusionIq.repository.SavedJobsRepository;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SavedJobsService {

    @Autowired
    private SavedJobsRepository savedJobsRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private JobRepository jobRepository;


    public SavedJobs saveJob(Long userId, Long jobId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Job> job = jobRepository.findById(jobId);

        if (user.isPresent() && job.isPresent()) {
            // Check if the SavedJobs entry already exists
            Optional<SavedJobs> existingSavedJob = savedJobsRepository.findByUserIdAndJobId(userId, jobId);

            if (existingSavedJob.isPresent()) {
                // If it exists, return a custom message or handle as needed
                throw new RuntimeException("Job is already saved by the user");
            }

            // If not, save the job
            SavedJobs savedJob = new SavedJobs(user.get(), job.get());
            return savedJobsRepository.save(savedJob);
        } else {
            throw new RuntimeException("User or Job not found"); // Customize error handling as needed
        }
    }

    public List<SavedJobs> getSavedJobsByUserId(Long userId) {
        return savedJobsRepository.findByUserId(userId);
    }

    @Transactional
    public void unsaveJob(Long jobId,Long UserId) {
        savedJobsRepository.deleteByJobIdAndUserId(jobId,UserId);
    }
}