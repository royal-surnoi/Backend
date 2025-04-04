package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.CourseDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CourseDocumentsRepo extends JpaRepository<CourseDocuments,Long> {
    List<CourseDocuments> findByCourseId(Long courseId);
    List<CourseDocuments> findByCourseIdIn(List<Long> courseIds);
}
