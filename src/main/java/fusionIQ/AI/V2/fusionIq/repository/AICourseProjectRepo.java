package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.AICourseProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AICourseProjectRepo extends JpaRepository<AICourseProject, Long> {

    @Query("SELECT p.aiCourseProjectUserAnswer FROM AICourseProject p WHERE p.id != :id")
    List<String> findAllAnswersExcept(@Param("id") Long id);

    List<AICourseProject> findByAiCoursePlanId(Long aiCoursePlanId);
}