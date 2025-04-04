package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.CourseVideoTrailer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseVideoTrailerRepo extends JpaRepository <CourseVideoTrailer,Long> {

    List<CourseVideoTrailer> findByCourseId(Long courseId);

}
