package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepo extends JpaRepository<Quiz,Long> {

    List<Quiz> findByLessonId(Long lessonId);

    long countByLessonModuleId(long lessonModuleId);

    @Query("SELECT q FROM Quiz q WHERE q.course IN (SELECT e.course FROM Enrollment e WHERE e.user.id = :userId) AND q.startDate > CURRENT_DATE")
    List<Quiz> findUpcomingQuizzesByUser(@Param("userId") Long userId);

    long countByLessonId(long lessonId);

    @Query("SELECT q FROM Quiz q JOIN FETCH q.lesson WHERE q.course.id = :courseId")
    List<Quiz> findByCourseIdWithLesson(@Param("courseId") Long courseId);

    @Query("SELECT q FROM Quiz q LEFT JOIN FETCH q.course WHERE q.id = :id")
    Optional<Quiz> findByIdWithCourse(@Param("id") Long id);

    List<Quiz> findByCourse(Course course);

    int countByCourseId(Long courseId);

    @Query("SELECT q FROM Quiz q " +
            "JOIN q.lesson l " +
            "JOIN l.lessonModule lm " +
            "JOIN lm.course c " +
            "JOIN Enrollment e ON e.course = c " +
            "WHERE e.user.id = :userId AND c.id = :courseId")
    List<Quiz> findQuizzesByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") Long userId);

    List<Quiz> findByStudent_IdAndCourse_Id(Long studentId, Long courseId);

    List<Quiz> findByCourseId(Long courseId);



    List<Quiz> findByLessonIdAndLessonModuleId(Long lessonId, Long lessonModuleId);




    List<Quiz> findByLessonIdAndUserId(Long lessonId, Long userId);

    @Query("SELECT 'Quiz' AS type, q.course.courseTitle, q.student, q.id FROM Quiz q WHERE q.teacher.id = :teacherId AND q.course.id = :courseId")
    List<Object[]> findQuizzesByTeacherIdAndCourseId(Long teacherId, Long courseId);

    List<Quiz> findByTeacherIdAndStudentId(Long teacherId, Long studentId);

    List<Quiz> findByCourseIdAndTeacherId(Long courseId, Long teacherId);



    List<Quiz> findByTeacherId(long teacherId);

    @Query("SELECT qz, c, qt FROM Quiz qz JOIN qz.course c LEFT JOIN Question qt ON qt.quiz.id = qz.id WHERE qz.id = :quizId")
    List<Object[]> findQuizDetailsByQuizId(Long quizId);
    void deleteById(Long id);


    List<Quiz> findByTeacherIdAndLessonIsNullAndLessonModuleIsNullAndStudentIdsIsNullAndStudentIsNullAndUserIsNull(Long teacherId);

}

