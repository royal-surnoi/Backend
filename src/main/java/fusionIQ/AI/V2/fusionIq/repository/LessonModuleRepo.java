package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.LessonModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LessonModuleRepo extends JpaRepository<LessonModule,Long> {
    List<LessonModule> findByCourseId(Long courseId);


    Optional<LessonModule> findByIdAndCourseId(Long id, Long courseId);

    void deleteByCourseId(Long id);
}
