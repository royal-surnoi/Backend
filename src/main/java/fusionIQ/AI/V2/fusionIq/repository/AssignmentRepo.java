package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Assignment;
import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.MockTestInterview;
import fusionIQ.AI.V2.fusionIq.data.SubmitAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AssignmentRepo extends JpaRepository<Assignment,Long> {


    List<Assignment> findByCourseId(Long courseId);


    List<Assignment> findAssignmentBylessonId(Long lessonId);

    List<Assignment> findByEndDateAfter(LocalDate endDate);

    List<Assignment> findByEndDate(LocalDate date);


    List<Assignment> findByCourse(Course course);


    List<Assignment> findByLessonIdAndLessonModuleId(Long lessonId, Long lessonModuleId);

    void deleteByCourseId(Long id);

    List<Assignment> findByLessonId(Long lessonId);


    List<Assignment> findByLessonModuleId(Long lessonModuleId);


    @Query("SELECT sa FROM SubmitAssignment sa WHERE sa.lesson.id = :lessonId AND sa.user.id = :userId")
    List<SubmitAssignment> findByLessonIdAndUserId(@Param("lessonId") Long lessonId, @Param("userId") Long userId);

    @Query("SELECT sa FROM SubmitAssignment sa WHERE sa.lessonModule.id = :lessonModuleId AND sa.user.id = :userId")
    List<SubmitAssignment> findByLessonModuleIdAndUserId(@Param("lessonModuleId") Long lessonModuleId, @Param("userId") Long userId);

    @Query("SELECT a FROM Assignment a WHERE a.course IN (SELECT e.course FROM Enrollment e WHERE e.user.id = :userId) AND a.startDate > CURRENT_DATE")
    List<Assignment> findUpcomingAssessmentsByUser(@Param("userId") Long userId);


    List<Assignment> findByLesson_Course_UserId(Long userId); // Assuming Course entity has a field userId


    @Query("SELECT a FROM Assignment a WHERE a.endDate > :currentDate AND a.id IN (SELECT s.assignment.id FROM SubmitAssignment s WHERE s.user.id = :userId)")
    List<Assignment> findUpcomingAssignmentsByUserId(@Param("userId") Long userId, @Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT a FROM Assignment a JOIN FETCH a.lesson WHERE a.course.id = :courseId")
    List<Assignment> findByCourseIdWithLessons(@Param("courseId") Long courseId);


    List<Assignment> findByTeacherId(Long teacherId);


    @Query("SELECT a FROM Assignment a WHERE a.id = :id AND a.teacher.id = :teacherId")
    Optional<Assignment> findByIdAndTeacherId(@Param("id") Long id, @Param("teacherId") Long teacherId);


    List<Assignment> findByCourseIdAndTeacherId(Long courseId, Long teacherId);


    boolean existsById(Long id);

    void deleteById(Long id);

    int countByCourseId(Long courseId);


    List<Assignment> findByCourseIdAndStudentId(Long courseId, Long studentId);

    List<Assignment> findByUserIdAndCourseId(Long userId, Long courseId);

    @Query("SELECT DISTINCT a.lessonModule.id FROM Assignment a WHERE a.course.id = :courseId")
    List<Long> findDistinctLessonModuleIdsByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(a) FROM Assignment a WHERE a.course.id = :courseId")
    long countTotalAssignmentsByCourseId(@Param("courseId") Long courseId);


    List<Assignment> findByCourse_Id(Long courseId);


    @Query("SELECT a.id, s.id, s.name FROM Assignment a JOIN a.student s WHERE a.teacher.id = :teacherId AND s.id = :studentId")
    List<Object[]> getAssignmentsAndStudentsByTeacherIdAndStudentId(@Param("teacherId") long teacherId, @Param("studentId") long studentId);


    @Query("SELECT 'Assignment' AS type, a.course.courseTitle, a.student, a.id FROM Assignment a WHERE a.teacher.id = :teacherId AND a.course.id = :courseId")
    List<Object[]> findAssignmentsByTeacherIdAndCourseId(Long teacherId, Long courseId);


    List<Assignment> findByTeacherIdAndStudentId(Long teacherId, Long studentId);

    @Query("SELECT LENGTH(a.studentIds) - LENGTH(REPLACE(a.studentIds, ',', '')) + 1 " +
            "FROM Assignment a WHERE a.id = :assignmentId AND a.teacher.id = :teacherId")
    int countStudentIdsByAssignmentIdAndTeacherId(@Param("assignmentId") Long assignmentId,
                                                  @Param("teacherId") Long teacherId);

    // Count the number of assignments in lessons that belong to a course
    @Query("SELECT COUNT(a) FROM Assignment a WHERE a.lesson.course.id = :courseId")
    long countAssignmentsInLessonsByCourse(Long courseId);


//    @Query("SELECT a FROM Assignment a JOIN FETCH a.student WHERE a.teacher.id = :teacherId AND a.student.id = :studentId")
//    List<Assignment> findByTeacherIdAndStudentId(@Param("teacherId") Long teacherId, @Param("studentId") Long studentId);


    @Query("SELECT a FROM Assignment a WHERE a.teacher.id = :teacherId AND a.student.id = :studentId")
    List<Assignment> findAssignmentsByTeacherIdAndStudentId(Long teacherId, Long studentId);

    List<Assignment> findByMockTestInterviewId(Long mockId);


    Optional<Assignment> findByMockTestInterview(MockTestInterview mockTestInterview);


    List<Assignment> findAllByMockTestInterviewId(Long mockTestInterviewId);


    @Query("SELECT a FROM Assignment a " +
            "WHERE a.teacher.id = :teacherId AND a.mockTestInterview.id = :mockId")
    List<Assignment> findAllByTeacherIdAndMockId(@Param("teacherId") Long teacherId, @Param("mockId") Long mockId);

    @Query("SELECT new map(sa.submitAssignment as submitAssignment, sa.userAssignmentAnswer as userAssignmentAnswer) " +
            "FROM SubmitAssignment sa " +
            "JOIN sa.assignment a " +
            "WHERE a.id = :assignmentId AND a.mockTestInterview.id = :mockId")
    List<Map<String, Object>> findSubmissionsByAssignmentIdAndMockId(@Param("assignmentId") Long assignmentId, @Param("mockId") Long mockId);

    int countByLessonId(Long lessonId);

    List<Assignment> findByTeacherIdAndCourseIdIsNotNullAndLessonIdIsNullAndLessonModuleIdIsNullAndStudentIdsIsNull(long teacherId);

    @Query("SELECT a FROM Assignment a " +
            "WHERE a.lesson.id = :lessonId " +
            "AND a.mockTestInterview IS NULL " +
            "AND a.studentIds IS NULL " +
            "AND a.student IS NULL")
    List<Assignment> findByLessonIdAndNullConditions(@Param("lessonId") Long lessonId);



    long countAssignmentsByCourseId(Long courseId);

    // Check if a user is enrolled in a course using a custom query
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Enrollment e WHERE e.user.id = :userId AND e.course.id = :courseId")
    boolean existsUserEnrolledInCourse(Long userId, Long courseId);

    @Query("SELECT a FROM Assignment a WHERE a.startDate > :currentDate AND a.user.id = :userId")
    List<Assignment> findAssignmentsByStartDateAfterAndUserId(@Param("currentDate") LocalDateTime currentDate, @Param("userId") Long userId);

    @Query("SELECT a FROM Assignment a WHERE a.startDate > CURRENT_TIMESTAMP AND a.user.id = :userId")
    List<Assignment> findFutureAssignmentsByUserId(@Param("userId") Long userId);




//    @Query("""
//        SELECT a
//        FROM Assignment a
//        WHERE a.course.id IN (
//            SELECT e.course.id
//            FROM Enrollment e
//            WHERE e.user.id = :userId
//        )
//          AND a.startDate > CURRENT_TIMESTAMP
//    """)
//    List<Assignment> findUpcomingAssignmentsByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT a 
        FROM Assignment a
        WHERE a.course.id IN (
            SELECT e.course.id 
            FROM Enrollment e 
            WHERE e.user.id = :userId
        )
          AND a.startDate > CURRENT_TIMESTAMP
    """)
    List<Assignment> findUpcomingAssignmentsByUserId(@Param("userId") Long userId);


}


















