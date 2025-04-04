package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.InstituteClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstituteClassRepo extends JpaRepository<InstituteClass,Long> {
    List<InstituteClass> findByTeacherId(Long teacherId);

    List<InstituteClass> findByInstituteId(Long instituteId);
}