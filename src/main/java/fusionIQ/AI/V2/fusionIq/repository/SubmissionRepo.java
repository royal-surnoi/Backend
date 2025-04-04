package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubmissionRepo extends JpaRepository<Submission,Long> {
    void deleteByActivityId(Long id);
    List<Submission> findByActivityId(Long activityId);
    List<Submission> findByUserId(Long userId);
    List<Submission> findByProjectId(Long projectId);

    void deleteByProjectId(Long id);

    void deleteByCourseId(Long id);


}
