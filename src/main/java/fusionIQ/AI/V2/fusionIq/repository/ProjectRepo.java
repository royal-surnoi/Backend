package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.MockTestInterview;
import fusionIQ.AI.V2.fusionIq.data.Project;
import fusionIQ.AI.V2.fusionIq.data.SubmitProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectRepo extends JpaRepository<Project,Long> {
    List<Project> findByCourseId(Long courseId);

    void deleteByCourseId(Long id);

    List<Project> findByCourse(Course course);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.course = :course")
    long countByCourse(@Param("course") Course course);


    @Query("SELECT p FROM Project p WHERE p.course IN (SELECT e.course FROM Enrollment e WHERE e.user.id = :userId) AND p.startDate > CURRENT_DATE")
    List<Project> findUpcomingProjectsByUser(@Param("userId") Long userId);

    @Query("SELECT p FROM Project p WHERE p.teacher.id = :teacherId")
    List<Project> findProjectsByTeacherId(@Param("teacherId") Long teacherId);

    @Query("SELECT p FROM Project p WHERE p.course.id = :courseId AND p.teacher.id = :teacherId")
    List<Project> findByCourseIdAndTeacherId(@Param("courseId") Long courseId, @Param("teacherId") Long teacherId);

    int countByCourseId(Long courseId);

    List<Project> findByCourseIdAndStudentId(Long courseId, Long studentId);

    List<Project> findByUserIdAndCourseId(Long userId, Long courseId);

    List<Project> findByStudent_IdAndCourse_Id(Long studentId, Long courseId);


    @Query("SELECT p.projectTitle, sp.student.id, sp.student.name, sp.submitProject " +
            "FROM Project p " +
            "JOIN SubmitProject sp ON sp.project.id = p.id " +
            "WHERE p.teacher.id = :teacherId AND sp.student.id = :studentId")
    List<Object[]> getProjectsByTeacherIdAndStudentId(@Param("teacherId") long teacherId, @Param("studentId") long studentId);

    List<Project> findByTeacherIdAndStudentId(Long teacherId, Long studentId);

    @Query("SELECT 'Project' AS type, p.course.courseTitle, p.student, p.id FROM Project p WHERE p.teacher.id = :teacherId AND p.course.id = :courseId")
    List<Object[]> findProjectsByTeacherIdAndCourseId(Long teacherId, Long courseId);

    @Query("SELECT p FROM Project p WHERE p.id = :projectId AND p.teacher.id = :teacherId")
    Project findByIdAndTeacherId(@Param("projectId") Long projectId, @Param("teacherId") Long teacherId);

    Optional<Project> findByMockTestInterviewId(Long mockId);

    Optional<Project> findByMockTestInterview(MockTestInterview mockTestInterview);

    List<Project> findAllByMockTestInterviewId(Long mockTestInterviewId);


    @Query("SELECT p FROM Project p " +
            "WHERE p.teacher.id = :teacherId AND p.mockTestInterview.id = :mockId")
    List<Project> findAllByTeacherIdAndMockId(@Param("teacherId") Long teacherId, @Param("mockId") Long mockId);

    @Query("SELECT new map(sp.submitProject as submitProject, sp.userAnswer as userAnswer) " +
            "FROM SubmitProject sp " +
            "JOIN sp.project p " +
            "WHERE p.id = :projectId AND p.mockTestInterview.id = :mockId")
    List<Map<String, Object>> findSubmissionsByProjectIdAndMockId(@Param("projectId") Long projectId, @Param("mockId") Long mockId);

    List<Project> findByTeacherIdAndCourseIdIsNotNullAndStudentIdsIsNull(long teacherId);

}








