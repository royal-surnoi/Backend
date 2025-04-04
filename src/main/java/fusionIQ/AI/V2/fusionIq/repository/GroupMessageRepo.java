package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.GroupMessages;
import fusionIQ.AI.V2.fusionIq.data.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupMessageRepo extends JpaRepository<GroupMessages, Long> {

    @Query("SELECT gm.admin.id FROM GroupMessages gm WHERE gm.id = :groupId")
    Long findAdminIdByGroupId(@Param("groupId") Long groupId);

    @Query("SELECT g FROM GroupMessages g LEFT JOIN FETCH g.members WHERE g.id = :id")
    Optional<GroupMessages> findByIdWithMembers(@Param("id") Long id);

    @Query("SELECT g FROM GroupMessages g LEFT JOIN FETCH g.members WHERE :user MEMBER OF g.members OR g.admin = :user")
    List<GroupMessages> findAllByMembersContaining(User user);
    @Modifying
    @Transactional
    @Query("DELETE FROM GroupMessages gm WHERE gm.id = :id")
    void deleteGroupMessagesById(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Message m WHERE m.groupMessages.id = :id")
    void deleteMessagesByGroupMessagesId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "update group_members set isAccepted = 1 where userId = :userId and groupMessagesId = :groupMessagesId;", nativeQuery = true)
    void acceptGroup(@Param("userId") Long userId, @Param("groupMessagesId") Long group_messages_id);


    @Query(value = "SELECT isAccepted FROM group_members WHERE userId = :userId AND groupMessagesId = :groupMessagesId LIMIT 1;", nativeQuery = true)
    Byte IsAcceptedGroup(@Param("userId") Long userId, @Param("groupMessagesId") Long groupMessagesId);

}
