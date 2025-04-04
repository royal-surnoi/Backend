package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.PersonalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalDetailsRepo extends JpaRepository<PersonalDetails, Long> {
    Optional<PersonalDetails> findByUserId(Long userId);

    @Query("SELECT pd.profession, pd.userLanguage, pd.userDescription, pd.age, pd.latitude, pd.longitude, pd.interests, " +
            "u.name, u.email, u.userImage, u.id " +
            "FROM PersonalDetails pd " +
            "JOIN pd.user u " +
            "WHERE u.id = :userId")
    List<Object[]> findPersonalDetailsAndUserFieldsByUserId(@Param("userId") Long userId);

    @Query("SELECT p.user.id, p.age, p.longitude, p.latitude, p.interests FROM PersonalDetails p")
    List<Object[]> findSelectedPersonalDetails();

}
