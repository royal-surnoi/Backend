package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);


    boolean existsByEmail(String email);
    User findByName(String name);

    @Query("SELECT u.id FROM User u")
    List<Long> findAllUserIds();



    @Query("SELECT DISTINCT a.student.id FROM Assignment a WHERE a.teacher.id = :teacherId")
    List<Long> findDistinctStudentIdsByTeacherId(Long teacherId);

//    @Query("SELECT DISTINCT u.id FROM User u JOIN u.enrollments e WHERE e.teacher.id = :teacherId")
//    List<Long> findDistinctStudentIdsByTeacherId(@Param("teacherId") Long teacherId);



}
