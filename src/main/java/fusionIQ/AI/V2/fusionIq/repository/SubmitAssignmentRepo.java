package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.SubmitAssignment;
import fusionIQ.AI.V2.fusionIq.data.SubmitProject;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface SubmitAssignmentRepo extends JpaRepository<SubmitAssignment,Long> {
    List<SubmitAssignment> findByLessonId(Long lessonId);

    List<SubmitAssignment> findByUserId(Long userId);

    Optional<Object> findByAssignmentId(Long assignmentId);

    @Query("SELECT s FROM SubmitAssignment s WHERE s.lesson.id = :lessonId AND s.user.id = :userId")
    List<SubmitAssignment> findByLessonIdAndUserId(Long lessonId, Long userId);


    @Query("SELECT sa FROM SubmitAssignment sa WHERE sa.lessonModule.id = :lessonModuleId AND sa.user.id = :userId")
    List<SubmitAssignment> findByLessonModuleIdAndUserId(@Param("lessonModuleId") Long lessonModuleId, @Param("userId") Long userId);

    @Query("SELECT sa FROM SubmitAssignment sa WHERE sa.user.id = :userId")
    List<SubmitAssignment> findAssignmentsByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT sa.user.id FROM SubmitAssignment sa WHERE sa.assignment.id = :assignmentId")
    List<Long> findUserIdsByAssignmentId(@Param("assignmentId") Long assignmentId);


    @Query("SELECT sa FROM SubmitAssignment sa WHERE sa.user.id = :userId AND sa.lesson.id = :lessonId")
    List<SubmitAssignment> findByUserIdAndLessonId(@Param("userId") Long userId, @Param("lessonId") Long lessonId);


    @Query("SELECT sa FROM SubmitAssignment sa WHERE sa.course.id = :courseId AND sa.user.id = :userId")
    List<SubmitAssignment> findByCourseIdAndUserId(Long courseId, Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM SubmitAssignment s WHERE s.assignment.id = :assignmentId")
    void deleteByAssignmentId(@Param("assignmentId") long assignmentId);


    int countByCourseIdAndUserId(Long courseId, Long userId);

    Optional<SubmitAssignment> findByAssignmentIdAndUserId(Long assignmentId, Long userId);

    long countByUserIdAndAssignmentCourseId(Long userId, Long courseId);

    @Query("SELECT COUNT(sa) FROM SubmitAssignment sa WHERE sa.course.id = :courseId AND sa.user.id = :userId")
    long countSubmittedAssignmentsByCourseIdAndUserId(@Param("courseId") Long courseId, @Param("userId") Long userId);


    List<SubmitAssignment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    List<SubmitAssignment> findByStudent_IdAndCourse_Id(Long studentId, Long courseId);


    boolean existsByUserIdAndAssignmentId(Long userId, Long assignmentId);

    boolean existsByStudentIdAndAssignmentId(Long studentId, Long assignmentId);


//    List<SubmitAssignment> findByLessonIdAndUserIdAndIsSubmittedTrue(Long lessonId, Long userId);


    @Query("SELECT sa FROM SubmitAssignment sa WHERE sa.student.id = :studentId")
    SubmitAssignment findByStudentId(@Param("studentId") long studentId);

    SubmitAssignment findByAssignmentIdAndStudentId(long id, Long studentId);

    @Query("SELECT COUNT(sa) FROM SubmitAssignment sa WHERE sa.course.id = :courseId AND sa.user.id = :userId")
    long countSubmittedAssignmentsByCourseAndUser(Long courseId, Long userId);

    boolean existsByUserIdAndAssignmentIdAndCourseId(Long userId, Long assignmentId, Long courseId);

    Optional<SubmitAssignment> findByUserIdAndAssignmentIdAndMockTestInterviewId(Long userId, Long assignmentId, Long mockId);

    @Query("SELECT new map(sa.submitAssignment as submitAssignment, sa.userAssignmentAnswer as userAssignmentAnswer, u.name as studentName, sa.submittedAt as submittedAt) " +
            "FROM SubmitAssignment sa " +
            "JOIN sa.assignment a " +
            "JOIN sa.student u " +  // Assuming 'student' is the field in SubmitAssignment that refers to User
            "WHERE a.teacher.id = :teacherId AND a.mockTestInterview.id = :mockId AND sa.assignment.id = a.id AND sa.mockTestInterview.id = :mockId")
    List<Map<String, Object>> findSubmissionsByTeacherIdAndMockId(@Param("teacherId") Long teacherId, @Param("mockId") Long mockId);

//    @Query("SELECT sa FROM SubmitAssignment sa " +
//            "JOIN sa.assignment a " +
//            "WHERE a.mockTestInterview.teacher.id = :teacherId")
//    List<SubmitAssignment> findAllByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT sa.id, sa.submittedAt, sa.userAssignmentAnswer, sa.submitAssignment, u.id, u.name, u.email " +
            "FROM SubmitAssignment sa " +
            "JOIN sa.student u " +  // Using the student field to refer to the User entity
            "WHERE sa.assignment.id = :assignmentId AND sa.isSubmitted = true")
    List<Object[]> findSubmittedAssignmentsAndStudentDetailsByAssignmentId(@Param("assignmentId") Long assignmentId);

//    List<SubmitAssignment> findByLessonIdAndUserIdAndIsSubmittedTrue(Long lessonId, Long userId);

    List<SubmitAssignment> findByLessonIdAndUserIdAndIsSubmittedTrueAndMockTestInterviewIsNull(Long lessonId, Long userId);

    boolean existsByUserIdAndAssignmentIdAndIsSubmittedTrue(Long userId, Long assignmentId);

    List<SubmitAssignment> findByLessonIdAndUserIdAndIsSubmittedTrue(Long lessonId, Long userId);

    List<SubmitAssignment> findByLessonIdAndUserIdAndMockTestInterviewIsNull(Long lessonId, Long userId);
    int countByLessonIdAndUserId(Long lessonId, Long userId);
    boolean existsByAssignmentIdAndUserId(Long assignmentId, Long userId);

    List<SubmitAssignment> findByLessonIdAndStudentIdAndIsSubmittedTrue(Long lessonId, Long studentId);

    @Query("SELECT sa.id, sa.userAssignmentAnswer, sa.user.id, a.assignmentDocument, a.id " +
            "FROM SubmitAssignment sa JOIN sa.assignment a " +
            "WHERE sa.assignment.id = :assignmentId AND sa.user.id = :userId")
    List<Object[]> findPartialByAssignmentIdAndUserId(@Param("assignmentId") Long assignmentId, @Param("userId") Long userId);

//    List<SubmitAssignment> findByLessonIdAndUserId(Long lessonId, Long userId);




    long countByUserIdAndAssignmentIdAndLessonId(Long userId, Long assignmentId, Long lessonId);
    @Query("SELECT COUNT(sa) FROM SubmitAssignment sa WHERE sa.user.id = :userId AND sa.assignment.id = :assignmentId AND sa.lesson.id = :lessonId")
    long countSubmittedAssignments(@Param("userId") Long userId, @Param("assignmentId") Long assignmentId, @Param("lessonId") Long lessonId);


    // Count the number of submitted assignments by userId and courseId
    @Query("SELECT COUNT(sa) FROM SubmitAssignment sa WHERE sa.user.id = :userId AND sa.assignment.course.id = :courseId")
    long countSubmittedAssignmentsByUserIdAndCourseId(Long userId, Long courseId);

    @Query("SELECT sa FROM SubmitAssignment sa " +
            "JOIN sa.assignment a " +
            "WHERE a.mockTestInterview.teacher.id = :teacherId")
    List<SubmitAssignment> findAllByTeacherId(@Param("teacherId") Long teacherId);

    List<SubmitAssignment> findByAssignmentTeacherIdAndIsSubmittedTrue(Long teacherId);

    List<SubmitAssignment> findByMockTestInterview_IdAndStudent_Id(Long mockId, Long studentId);

}





