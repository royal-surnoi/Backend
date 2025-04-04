package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepo extends JpaRepository<Activity,Long> {
    List<Activity> findByLessonId(Long lessonId);

    void deleteByLessonId(Long id);
}
