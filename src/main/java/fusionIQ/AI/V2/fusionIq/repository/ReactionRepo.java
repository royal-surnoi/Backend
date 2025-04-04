package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.Message;
import fusionIQ.AI.V2.fusionIq.data.Reactions;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public interface ReactionRepo extends JpaRepository<Reactions, Long> {
    Optional<Reactions> findByMessageAndUser(Message message, User user);

    @Modifying
    @Query("delete from Reactions r where r.message.id = :messageId and r.user.id = :userId")
    void deleteData(@Param("messageId") Long messageId, @Param("userId") Long userId);


}
