package fusionIQ.AI.V2.fusionIq.repository;


import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepo  extends JpaRepository<Rating,Long> {
    void deleteByCourseId(Long id);

    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.course.id = :courseId")
    Double calculateAverageRatingByCourseId(@Param("courseId") Long courseId);


    List<Rating> findByCourseId(Long courseId);

    List<Rating> findByCourse(Course course);

}
