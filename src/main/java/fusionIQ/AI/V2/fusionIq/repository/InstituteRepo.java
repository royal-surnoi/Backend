package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Institute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InstituteRepo extends JpaRepository<Institute, Long> {

    List<Institute> findByInstituteType(String instituteType);

    @Query("SELECT i FROM Institute i WHERE " +
            "(:pincode IS NULL OR i.pincode = :pincode) AND " +
            "(:institute_Name IS NULL OR i.institute_Name LIKE %:institute_Name%) AND " +
            "(:instituteType IS NULL OR i.instituteType = :instituteType) AND " +
            "(:location IS NULL OR i.location LIKE %:location%) AND " +
            "(:establishedIn IS NULL OR i.establishedIn = :establishedIn)")
    List<Institute> findInstitutesByCriteria(String pincode, String institute_Name, String instituteType, String location, Integer establishedIn);


    @Query("SELECT i FROM Institute i WHERE i.user.id = :userId")
    List<Institute> findByUserId(@Param("userId") Long userId);

}

