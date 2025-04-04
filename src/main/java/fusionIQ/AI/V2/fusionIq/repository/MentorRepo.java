package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Mentor;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MentorRepo extends JpaRepository<Mentor, Long> {
    Optional<Object> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUserId(Long userId);

    Optional<Mentor> findByUserId(Long userId);

    @Query("SELECT new map(" +
            "m.mentorId as mentorId, " +
            "m.username as username, " +
            "m.password as password, " +
            "m.user.id as userId, " +
            "m.user.userImage as userImage, " +
            "m.user.name as name) " +
            "FROM Mentor m " +
            "JOIN Follow f ON f.following.id = m.user.id " +
            "WHERE f.follower.id = :userId")
    List<Map<String, Object>> findMentorsFollowedByUser(@Param("userId") Long userId);

    Optional<Mentor> findByUser(User user);
}
