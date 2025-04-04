package fusionIQ.AI.V2.fusionIq.repository;

import fusionIQ.AI.V2.fusionIq.data.CommunityMessages;
import fusionIQ.AI.V2.fusionIq.data.GroupMessages;
import fusionIQ.AI.V2.fusionIq.data.Message;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message,Long> {
    List<Message> findBySenderId(Long senderId);
    List<Message> findByReceiverId(Long receiverId);
    List<Message> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    //    @Query("SELECT m FROM Message m WHERE (m.sender.id = :senderId AND m.receiver.id = :receiverId) OR (m.sender.id = :receiverId AND m.receiver.id = :senderId) ORDER BY m.createdAt")
//    List<Message> findConversation(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
    @Modifying
    @Query("UPDATE Message m SET m.isRead = true WHERE m.id = :messageId")
    void markMessageAsRead(@Param("messageId") Long messageId);
    @Query("SELECT m FROM Message m WHERE m.groupMessages.id = :groupMessagesId ORDER BY m.createdAt DESC")
    List<Message> findByGroupIdOrderByCreatedAtDesc(@Param("groupMessagesId") Long groupMessagesId);


//    List<Message> findByReceiverIdAndIsReadFalse(Long receiverId);

    List<Message> findByGroupMessagesOrderByCreatedAtAsc(GroupMessages groupMessages);


    Message findByIdAndSenderId(Long messageId, Long senderId);

    List<Message> findAllBySenderIdAndReceiverId(Long senderId, Long receiverId);

//    Long countByReceiverIdAndIsReadFalse(Long userId);

    @Query("SELECT m FROM Message m WHERE ((m.sender.id = :senderId AND m.receiver.id = :receiverId) " +
            "OR (m.sender.id = :receiverId AND m.receiver.id = :senderId)) " +
            "AND m.senderDeleted = false AND m.receiverDeleted = false")
    List<Message> findConversation(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

    List<Message> findByReceiverIdAndIsReadFalse(Long receiverId);
    long countByReceiverIdAndIsReadFalse(Long userId);

    long countDistinctSendersByReceiverIdAndIsReadFalse(Long userId);

    long countByReceiverIdAndSenderIdAndIsReadFalse(Long userId, long id);

    List<Message> findAllBySenderIdAndReceiverIdAndSenderDeletedFalse(Long senderId, Long receiverId);

    List<Message> findAllBySenderIdAndReceiverIdAndReceiverDeletedFalse(Long senderId, Long receiverId);


    @Transactional
    @Modifying
    @Query("update Message m set m.sender_reaction = :reaction where m.id = :messageId")
    void setSenderReaction(@Param("messageId") Long messageId, @Param("reaction") String reaction);

    @Transactional
    @Modifying
    @Query("update Message m set m.reciver_reaction = :reaction where m.id = :messageId")
    void setReceiverReaction(@Param("messageId") Long messageId, @Param("reaction") String reaction);

    @Transactional
    @Modifying
    @Query("update Message m set m.sender_reaction = null where m.id = :messageId")
    void removeSenderReaction(@Param("messageId") Long messageId);

    @Transactional
    @Modifying
    @Query("update Message m set m.reciver_reaction = null where m.id = :messageId")
    void removeReceiverReaction(@Param("messageId") Long messageId);

    @Query("SELECT m FROM Message m JOIN FETCH m.sender WHERE m.communityMessages = :community ORDER BY m.createdAt ASC")
    List<Message> findByCommunityMessagesOrderByCreatedAtAsc(@Param("community") CommunityMessages communityMessages);



}
