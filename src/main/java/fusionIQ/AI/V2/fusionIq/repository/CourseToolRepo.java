package fusionIQ.AI.V2.fusionIq.repository;


import fusionIQ.AI.V2.fusionIq.data.CourseTools;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseToolRepo extends JpaRepository<CourseTools,Long> {

    List<CourseTools> findToolsByCourseId(Long courseId);

    Optional<CourseTools> findByCourseId(Long courseId);

    void deleteByCourseId(Long id);
}