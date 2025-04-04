//package fusionIQ.AI.V2.fusionIq.service;
//
//import fusionIQ.AI.V2.fusionIq.data.GroupMessages;
//import fusionIQ.AI.V2.fusionIq.data.Message;
//import fusionIQ.AI.V2.fusionIq.data.User;
//import fusionIQ.AI.V2.fusionIq.repository.GroupMessageRepo;
//import fusionIQ.AI.V2.fusionIq.repository.MessageRepo;
//import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//@EnableKafka
//@Service
//public class KafkaConsumerService {
//
//    @Autowired
//    private MessageRepo messageRepo;
//
//    @Autowired
//    private GroupMessageRepo groupMessageRepo;
//
//    @Autowired
//    private UserRepo userRepo;
//
//    @Autowired
//    private ChatService chatService;
//
//    // 1. Kafka listener for consuming messages from 'chat-messages' topic
//    @KafkaListener(topics = "chat-messages", containerFactory = "kafkaListenerContainerFactory")
//    public void consumeMessages(List<ConsumerRecord<String, Message>> records) {
//        List<Message> messages = records.stream()
//                .map(ConsumerRecord::value)
//                .collect(Collectors.toList());
//        // Save the batch of messages to the database
//        messageRepo.saveAll(messages);
//        System.out.println("Batch of messages processed: " + messages.size());
//    }
//
//
//
//    // Kafka listener for consuming group creation requests from 'create-group' topic
//    @KafkaListener(topics = "create-group", groupId = "group-service")
//    public void consumeGroupCreationRequest(ConsumerRecord<String, Object[]> record) {
//        Object[] values = record.value();
//
//        // Extract the values from the Kafka message
//        String name = (String) values[0];
//        Long adminId = (Long) values[1];
//        List<Long> memberIds = (List<Long>) values[2];
//
//        // Create the group using the service method
//        chatService.createGroup(name, adminId, memberIds);
//
//        System.out.println("Group created: " + name);
//    }
//
//    @KafkaListener(topics = "delete-message", groupId = "delete-group")
//    public void consumeDeleteRequest(Object[] deleteRequest) {
//        Long senderId = (Long) deleteRequest[0];
//        Long messageId = (Long) deleteRequest[1];
//
//        boolean isDeleted = chatService.deleteMessageBySenderAndMessageId(senderId, messageId);
//
//        if (isDeleted) {
//            System.out.println("Message deleted: messageId = " + messageId);
//        } else {
//            System.out.println("Failed to delete message: messageId = " + messageId);
//        }
//    }
//
//    @KafkaListener(topics = "delete-chat", groupId = "delete-group")
//    public void consumeChatDeleteRequest(Object[] deleteRequest) {
//        Long userId = (Long) deleteRequest[0];
//        Long otherUserId = (Long) deleteRequest[1];
//
//        boolean isDeleted = chatService.deleteChatByUser(userId, otherUserId);
//
//        if (isDeleted) {
//            System.out.println("Chat deleted between userId = " + userId + " and otherUserId = " + otherUserId);
//        } else {
//            System.out.println("Failed to delete chat between userId = " + userId + " and otherUserId = " + otherUserId);
//        }
//    }
//}