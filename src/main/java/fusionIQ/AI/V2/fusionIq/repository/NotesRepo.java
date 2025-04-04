package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepo extends JpaRepository<Notes,Long> {
    List<Notes> findByUserIdAndCourseId(Long userId, Long courseId);
    List<Notes> findByCourseId(Long courseId);

    List<Notes> findByUserIdAndCourseIdAndLessonId(Long userId, Long courseId, Long lessonId);

    List<Notes> findByUserId(Long userId);

    List<Notes> findByLessonId(Long lessonId);


}