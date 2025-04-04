package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Assignment;
import fusionIQ.AI.V2.fusionIq.data.Project;
import fusionIQ.AI.V2.fusionIq.data.TeacherFeedback;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherFeedbackRepo extends JpaRepository<TeacherFeedback, Long> {



    List<TeacherFeedback> findByStudentId(Long studentId);

    @Transactional
    @Modifying
    @Query("DELETE FROM TeacherFeedback t WHERE t.assignment.id = :assignmentId")
    void deleteByAssignmentId(@Param("assignmentId") long assignmentId);

    void deleteByProjectId(Long projectId);

    Optional<TeacherFeedback> findByStudentIdAndAssignmentId(Long studentId, Long assignmentId);

    Optional<TeacherFeedback> findByTeacherIdAndStudentIdAndQuizId(Long teacherId, Long studentId, Long quizId);

    Optional<Object> findByTeacherIdAndCourseIdAndLessonIdAndStudentIdAndQuizId(Long teacherId, Long courseId, Long lessonId, Long studentId, Long quizId);

    @Query("SELECT tf FROM TeacherFeedback tf WHERE " +
            "(:teacherId IS NULL OR tf.teacher.id = :teacherId) AND " +
            "(:studentId IS NULL OR tf.student.id = :studentId) AND " +
            "(:courseId IS NULL OR tf.course.id = :courseId) AND " +
            "(:quizId IS NULL OR tf.quiz.id = :quizId) AND " +
            "(:assignmentId IS NULL OR tf.assignment.id = :assignmentId) AND " +
            "(:projectId IS NULL OR tf.project.id = :projectId) AND " +
            "(:lessonId IS NULL OR tf.lesson.id = :lessonId) AND " +
            "(:lessonModuleId IS NULL OR tf.lessonModule.id = :lessonModuleId)")
    List<TeacherFeedback> findAllByParams(@Param("teacherId") Long teacherId,
                                          @Param("studentId") Long studentId,
                                          @Param("courseId") Long courseId,
                                          @Param("quizId") Long quizId,
                                          @Param("assignmentId") Long assignmentId,
                                          @Param("projectId") Long projectId,
                                          @Param("lessonId") Long lessonId,
                                          @Param("lessonModuleId") Long lessonModuleId);

//    @Query("SELECT tf.assignment.id FROM TeacherFeedback tf WHERE tf.teacher.id = :teacherId AND tf.assignment IS NOT NULL")
//    List<Long> findAssignmentIdsByTeacherId(@Param("teacherId") Long teacherId);
//
//    @Query("SELECT tf.project.id FROM TeacherFeedback tf WHERE tf.teacher.id = :teacherId AND tf.project IS NOT NULL")
//    List<Long> findProjectIdsByTeacherId(@Param("teacherId") Long teacherId);

    List<TeacherFeedback> findByStudent_IdAndAssignment_Id(Long studentId, Long assignmentId);

    // Query for quiz feedback using student.id
    List<TeacherFeedback> findByStudent_IdAndQuiz_Id(Long studentId, Long quizId);

    // Query for project feedback using student.id
    List<TeacherFeedback> findByStudent_IdAndProject_Id(Long studentId, Long projectId);

    @Query("SELECT tf.assignment.id FROM TeacherFeedback tf WHERE tf.teacher.id = :teacherId AND tf.assignment IS NOT NULL")
    List<Long> findAssignmentIdsByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT tf.project.id FROM TeacherFeedback tf WHERE tf.teacher.id = :teacherId AND tf.project IS NOT NULL")
    List<Long> findProjectIdsByTeacherId(@Param("teacherId") Long teacherId);

    boolean existsByProjectAndTeacherId(Project project, Long teacherId);

    boolean existsByAssignmentAndTeacherId(Assignment assignment, Long teacherId);

}

