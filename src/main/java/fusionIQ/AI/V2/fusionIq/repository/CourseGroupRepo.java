package fusionIQ.AI.V2.fusionIq.repository;


import fusionIQ.AI.V2.fusionIq.data.CourseGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseGroupRepo extends JpaRepository<CourseGroup, Long> {


    List<CourseGroup> findByTeacherId(Long teacherId);


    Optional<CourseGroup> findByGroupName(String groupName);


    void deleteByGroupName(String groupName);


    List<CourseGroup> findByCreatedAtAfter(LocalDateTime createdAt);


    List<CourseGroup> findByGroupNameContainingIgnoreCase(String keyword);

    List<CourseGroup> findByTeacherIdAndCreatedAtAfter(Long teacherId, LocalDateTime createdAt);
}
