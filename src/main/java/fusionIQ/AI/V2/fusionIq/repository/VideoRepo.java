package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Lesson;
import fusionIQ.AI.V2.fusionIq.data.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VideoRepo extends JpaRepository<Video,Long> {
    List<Video> findByCourse(Course course);


    List<Video> findByCourseId(Long courseId);
    List<Video> findByLesson(Lesson lesson);


    Optional<Video> getVideoById(Long id);

    List<Video> findByCourseId(long courseId);

    long countByLessonId(long lessonId);


    @Query("SELECT COUNT(v) FROM Video v WHERE v.course.id = :courseId")
    long countByCourseId(@Param("courseId") long courseId);


    @Query("SELECT v FROM Video v JOIN v.course c WHERE c.user.id = :userId AND c.level = :level")
    List<Video> findByUserIdAndLevel(@Param("userId") Long userId, @Param("level") Course.Level level);

    @Modifying
    @Query("DELETE FROM Video v WHERE v.course.id = :courseId")
    void deleteByCourseId(@Param("courseId") Long courseId);

    List<Video> findByCourseIdAndCourseCourseTitleAndCourseLevel(Long courseId, String courseTitle, Course.Level level);

    @Query("SELECT COUNT(v) FROM Video v WHERE v.lesson.lessonModule.id = :lessonModuleId")
    long countByLessonModuleId(@Param("lessonModuleId") Long lessonModuleId);


    @Query("SELECT v FROM Video v WHERE v.lesson.id = :lessonId")
    List<Video> findByLessonId(@Param("lessonId") Long lessonId);


    List<Video> findByLessonModuleId(Long lessonModuleId);

    @Query("SELECT v.videoTitle, v.videoDescription, v.transcript " +
            "FROM Video v WHERE v.course.id = :courseId AND v.lesson.id = :lessonId")
    List<Object[]> findVideoDetailsByCourseIdAndLessonId(@Param("courseId") Long courseId, @Param("lessonId") Long lessonId);
}
