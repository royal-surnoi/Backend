package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.InstituteClassRoomStudents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstituteClassRoomStudentsRepository extends JpaRepository<InstituteClassRoomStudents, Long> {
    Optional<InstituteClassRoomStudents> findByStudentIdAndClassroom_Id(Long studentId, Long classroomId);
    @Query("SELECT icrs FROM InstituteClassRoomStudents icrs WHERE icrs.student.id = :studentId")
    List<InstituteClassRoomStudents> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT icrs FROM InstituteClassRoomStudents icrs WHERE icrs.classroom.id = :classId")
    List<InstituteClassRoomStudents> findByClassId(@Param("classId") Long classId);
}

