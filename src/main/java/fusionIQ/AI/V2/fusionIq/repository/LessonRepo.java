package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LessonRepo extends JpaRepository<Lesson,Long> {
    List<Lesson> findByCourseId(Long courseId);

    void deleteByCourseId(Long courseId);


    @Query("SELECT l FROM Lesson l WHERE l.lessonModule.id = :moduleId")
    List<Lesson> findByLessonModuleId(@Param("moduleId") Long moduleId);


}
