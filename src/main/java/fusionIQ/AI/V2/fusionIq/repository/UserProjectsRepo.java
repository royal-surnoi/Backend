package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.UserProjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserProjectsRepo extends JpaRepository<UserProjects, Long> {
    //    List<UserProjects> findByUserProfileId(Long userProfileId);
    List<UserProjects> findByUser_Id(Long userId);
    List<UserProjects> findByUserId(Long userId);
}


