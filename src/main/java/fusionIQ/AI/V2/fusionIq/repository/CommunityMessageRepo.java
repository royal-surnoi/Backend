package fusionIQ.AI.V2.fusionIq.repository;


import fusionIQ.AI.V2.fusionIq.data.CommunityMessages;
import fusionIQ.AI.V2.fusionIq.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommunityMessageRepo extends JpaRepository<CommunityMessages, Long> {


    @Query("SELECT g FROM CommunityMessages g LEFT JOIN FETCH g.members WHERE :user MEMBER OF g.members")
    List<CommunityMessages> findAllByMembersContaining(User user);


}