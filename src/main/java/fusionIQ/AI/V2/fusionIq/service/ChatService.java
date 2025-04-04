package fusionIQ.AI.V2.fusionIq.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import fusionIQ.AI.V2.fusionIq.data.*;

import fusionIQ.AI.V2.fusionIq.repository.*;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.Cacheable;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GroupMessageRepo groupMessageRepo;
    @Autowired
    private BlockRepository blockRepo;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepo notificationRepo;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CommunityMessageRepo communityMessageRepo;




    private Map<Long, Boolean> onlineStatusMap = new HashMap<>();
    private final String bucketName = "fusion-v2-test1";
    private final String folderName = "Chat/";

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Path tempFile = Files.createTempFile("temp", file.getOriginalFilename());
        Files.write(tempFile, file.getBytes());

        amazonS3.putObject(new PutObjectRequest(bucketName, folderName + fileName, tempFile.toFile()));

        Files.delete(tempFile);

        return amazonS3.getUrl(bucketName, folderName + fileName).toExternalForm();
    }

    public Message saveMessage(User sender, User receiver, String messageContent, MultipartFile file) throws IOException {
        if (blockRepo.existsByBlockerAndBlocked(receiver, sender)) {
            throw new RuntimeException("Sender is blocked by the receiver");
        }
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessageContent(messageContent);
        message.setRead(false);
        message.setCreatedAt(LocalDateTime.now());

        if (file != null && !file.isEmpty()) {
            String fileUrl = uploadFile(file);
            message.setFileUrl(fileUrl);
        }

        messageRepo.save(message);

        // Invalidate both the messagesCache and conversationsCache
        Cache cache = cacheManager.getCache("messagesCache");
        if (cache != null) {
            cache.evict(receiver.getId());
        }
        Cache messagesCache = cacheManager.getCache("messagesCache");
        if (messagesCache != null) {
            messagesCache.evict(receiver.getId());
        }

        Cache conversationsCache = cacheManager.getCache("conversationsCache");
        if (conversationsCache != null) {
            conversationsCache.evict(Arrays.asList(sender.getId(), receiver.getId()));
            conversationsCache.evict(Arrays.asList(receiver.getId(), sender.getId()));
        }

        return message;
    }




    @Cacheable(value = "messagesCache", key = "#userId")
    public List<Message> getMessages(Long userId) throws IOException {
        List<Message> messages = new ArrayList<>();
        ListObjectsV2Result result = amazonS3.listObjectsV2(bucketName, folderName + userId + "/");
        List<S3ObjectSummary> objects = result.getObjectSummaries();

        for (S3ObjectSummary os : objects) {
            S3Object s3Object = amazonS3.getObject(bucketName, os.getKey());
            InputStream inputStream = s3Object.getObjectContent();
            Message message = objectMapper.readValue(inputStream, Message.class);
            messages.add(message);
        }

        messages.sort(Comparator.comparing(Message::getCreatedAt).reversed());

        return messages;
    }




    //    public List<Message> getConversation(Long senderId, Long receiverId) {
//        validateUser(senderId);
//        validateUser(receiverId);
//        return messageRepo.findConversation(senderId, receiverId);
//    }

    @Cacheable(value = "conversationsCache", key = "T(java.util.Arrays).asList(#senderId, #receiverId)")
    public List<Message> getConversation(Long senderId, Long receiverId) {
        validateUser(senderId);
        validateUser(receiverId);

        // Fetch messages sent by the sender and not marked as deleted by them
        List<Message> sentMessages = messageRepo.findAllBySenderIdAndReceiverIdAndSenderDeletedFalse(senderId, receiverId);

        // Fetch messages received by the sender and not marked as deleted by them
        List<Message> receivedMessages = messageRepo.findAllBySenderIdAndReceiverIdAndReceiverDeletedFalse(receiverId, senderId);

        List<Message> conversation = new ArrayList<>();
        conversation.addAll(sentMessages);
        conversation.addAll(receivedMessages);

        // Set the 'sent' flag accordingly
        for (Message message : conversation) {
            if (message.getSender().getId()==(senderId)) {
                message.setSent(true);
            } else {
                message.setSent(false);
            }
        }

        return conversation;
    }


    private void validateUser(Long userId) {
        if (userId == null || !userRepo.existsById(userId)) {
            throw new IllegalArgumentException("Invalid user ID: " + userId);
        }
    }
    public boolean isUserOnline(Long userId) {
        return onlineStatusMap.getOrDefault(userId, false);
    }

    public void setUserOnline(Long userId, boolean online) {
        onlineStatusMap.put(userId, online);
    }


    public GroupMessages addUserToGroup(Long groupMessagesId, User user) {
        GroupMessages group = groupMessageRepo.findById(groupMessagesId).orElseThrow();
        group.getMembers().add(user);
        return groupMessageRepo.save(group);
    }

    public GroupMessages removeUserFromGroup(Long groupMessagesId, User user) {
        GroupMessages group = groupMessageRepo.findById(groupMessagesId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<User> memberList = new ArrayList<>(group.getMembers());

        memberList.remove(user);

        group.setMembers((Set<User>) new HashSet<>(memberList));

        return groupMessageRepo.save(group);
    }




    public Message saveGroupMessage(Long groupId, Long senderId, String messageContent, MultipartFile file) throws IOException {
        GroupMessages group = groupMessageRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User sender = userRepo.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Message message = new Message();
        message.setMessageContent(messageContent);
        message.setCreatedAt(LocalDateTime.now());
        message.setSender(sender);
        message.setGroupMessages(group);

        if (file != null && !file.isEmpty()) {
            String fileUrl = uploadFile(file);
            message.setFileUrl(fileUrl);
        }
        return messageRepo.save(message);
    }

    public List<Message> getGroupMessages(Long groupId) {
        GroupMessages group = groupMessageRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<Message> messages = messageRepo.findByGroupMessagesOrderByCreatedAtAsc(group);

        if (messages.isEmpty()) {
            System.out.println("No messages found for group ID: " + groupId);
        } else {
            for (Message message : messages) {
                System.out.println("Message ID: " + message.getId() + ", Sender: " + message.getSender() + ", Receiver: " + message.getReceiver());
            }
        }

        return messages;
    }

    public List<GroupMember> getGroupMembersWithAdminStatus(Long groupId) {
        Optional<GroupMessages> optionalGroupMessages = groupMessageRepo.findById(groupId);
        if (optionalGroupMessages.isPresent()) {
            GroupMessages groupMessages = optionalGroupMessages.get();
            Set<User> members = groupMessages.getMembers();
            User admin = groupMessages.getAdmin();
            return members.stream()
                    .map(member -> new GroupMember(
                            member.getId(),
                            member.getName(),
                            member.getEmail(),
                            member.equals(admin)))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }


    public Long isadmin(Long id){
        return groupMessageRepo.findAdminIdByGroupId(id);
    }

    @Transactional
    public GroupMessages createGroup(String name, Long adminId, List<Long> memberIds) {
        GroupMessages group = new GroupMessages();
        group.setName(name);
        User admin = userRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        group.setAdmin(admin);

        Set<User> members = new HashSet<>();
        for (Long memberId : memberIds) {
            User member = userRepo.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("Member not found"));
            members.add(member);
        }
        group.setMembers(members);

        return groupMessageRepo.save(group);
    }

    @Transactional
    public GroupMessages getGroup(Long groupMessagesId) {
        return groupMessageRepo.findByIdWithMembers(groupMessagesId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
    }
    public List<GroupMessages> getGroupsForUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return groupMessageRepo.findAllByMembersContaining(user);
    }
    public void deletegroup(long groupid){
        groupMessageRepo.deleteMessagesByGroupMessagesId(groupid);
        groupMessageRepo.deleteGroupMessagesById(groupid);
    }

    public boolean deleteMessageBySenderAndMessageId(Long senderId, Long messageId) {
        Message message = messageRepo.findByIdAndSenderId(messageId, senderId);
        if (message != null) {
            messageRepo.delete(message);
            return true;
        }
        return false;
    }


    public boolean deleteChatByUser(Long userId, Long otherUserId) {
        List<Message> messages = messageRepo.findAllBySenderIdAndReceiverIdAndSenderDeletedFalse(userId, otherUserId);
        List<Message> messagesAsReceiver = messageRepo.findAllBySenderIdAndReceiverIdAndReceiverDeletedFalse(otherUserId, userId);

        boolean anyMessageDeleted = false;

        // Handling messages where the current user is the sender
        for (Message message : messages) {
            if (message.getSender().getId()==(userId)) {
                message.setSenderDeleted(true);
            }

            // Check if both sender and receiver have deleted the message
            if (message.isSenderDeleted() && message.isReceiverDeleted()) {
                messageRepo.delete(message);
            } else {
                messageRepo.save(message);
            }

            anyMessageDeleted = true;
        }

        // Handling messages where the current user is the receiver
        for (Message message : messagesAsReceiver) {
            if (message.getReceiver().getId()==(userId)) {
                message.setReceiverDeleted(true);
            }

            // Check if both sender and receiver have deleted the message
            if (message.isSenderDeleted() && message.isReceiverDeleted()) {
                messageRepo.delete(message);
            } else {
                messageRepo.save(message);
            }
            anyMessageDeleted = true;
        }
        return anyMessageDeleted;
    }




    public List<Message> getContactsWithUnreadCounts(Long receiverId) {
        List<Message> contacts = messageRepo.findByReceiverId(receiverId);
        for (Message contact : contacts) {
            long unreadCount = messageRepo.countByReceiverIdAndSenderIdAndIsReadFalse(receiverId, contact.getId());
            contact.setUnreadMessageCount(unreadCount);
        }
        return contacts;
    }


    public long getUnreadSendersCount(Long userId) {
        return messageRepo.countDistinctSendersByReceiverIdAndIsReadFalse(userId);
    }
    public List<Message> getUnreadMessages(Long receiverId) {
        return messageRepo.findByReceiverIdAndIsReadFalse(receiverId);
    }

    public void markAsRead(Long messageId) {
        Message message = messageRepo.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        message.setRead(true);
        messageRepo.save(message);
    }

    public long getUnreadMessageCount(Long userId) {
        return messageRepo.countByReceiverIdAndIsReadFalse(userId);
    }

    public void markAllAsRead(Long userId) {
        List<Message> unreadMessages = messageRepo.findByReceiverIdAndIsReadFalse(userId);
        for (Message message : unreadMessages) {
            message.setRead(true);
        }
        messageRepo.saveAll(unreadMessages);
    }


    public boolean acceptGroup(Long userId, Long GroupId){
        groupMessageRepo.acceptGroup(userId,GroupId);
        return true;
    }

    public Byte isAccepted(Long userId, Long GroupId){
        return groupMessageRepo.IsAcceptedGroup(userId,GroupId);
    }

    public ResponseEntity<Void> setReactions(String type, Long MessageId, String Reaction){
        if(type.equals("Sender")){
            messageRepo.setSenderReaction(MessageId,Reaction);
        }
        else {
            messageRepo.setReceiverReaction(MessageId,Reaction);
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> removeReactions(String type, Long MessageId){
        if(type.equals("Sender")){
            messageRepo.removeSenderReaction(MessageId);
        }
        else {
            messageRepo.removeReceiverReaction(MessageId);
        }
        return ResponseEntity.ok().build();
    }

    public CommunityMessages addUserToCommunity(Long CommunityMessagesId, User user) {
        CommunityMessages Community = communityMessageRepo.findById(CommunityMessagesId).orElseThrow();
        Community.getMembers().add(user);
        return communityMessageRepo.save(Community);
    }


    public CommunityMessages removeUserFromCommunity(Long CommunityMessagesId, User user) {
        CommunityMessages Community = communityMessageRepo.findById(CommunityMessagesId)
                .orElseThrow(() -> new RuntimeException("Group not found"));




        List<User> memberList = new ArrayList<>(Community.getMembers());

        memberList.remove(user);

        // If the user being removed is the admin
        if (Community.getAdmin().equals(user)) {
            if (!Community.getMembers().isEmpty()) {
                // Assign a new admin from the members
                User newAdmin = Community.getMembers().iterator().next();
                Community.setAdmin(newAdmin);
            } else {
                // If no members are left, set admin to null (or handle this scenario as needed)
                Community.setAdmin(null);
            }
        }
        Community.setMembers((Set<User>) new HashSet<>(memberList));

        return communityMessageRepo.save(Community);
    }


    public Message saveCommunityMessage(Long CommunityId, Long senderId, String messageContent, MultipartFile file) throws IOException {
        CommunityMessages Community = communityMessageRepo.findById(CommunityId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User sender = userRepo.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Message message = new Message();
        message.setMessageContent(messageContent);
        message.setCreatedAt(LocalDateTime.now());
        message.setSender(sender);
        message.setCommunityMessages(Community);

        if (file != null && !file.isEmpty()) {
            String fileUrl = uploadFile(file);
            message.setFileUrl(fileUrl);
        }
        return messageRepo.save(message);
    }

    public List<Message> getCommunityMessages(Long CommunityId) {
        CommunityMessages Community = communityMessageRepo.findById(CommunityId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<Message> messages = messageRepo.findByCommunityMessagesOrderByCreatedAtAsc(Community);

        if (messages.isEmpty()) {
            System.out.println("No messages found for group ID: " + CommunityId);
        } else {
            for (Message message : messages) {
                // Avoid directly printing the entire object or recursive fields
                System.out.println("Message ID: " + message.getId()
                        + ", Sender ID: " + (message.getSender() != null ? message.getSender().getId() : "N/A")
                        + ", Receiver ID: " + (message.getReceiver() != null ? message.getReceiver().getId() : "N/A"));
            }
        }

        return messages;
    }


    public List<CommunityMember> getCommunityMembersWithAdminStatus(Long CommunityId) {
        Optional<CommunityMessages> optionalCommunityMessages = communityMessageRepo.findById(CommunityId);
        if (optionalCommunityMessages.isPresent()) {
            CommunityMessages communityMessages = optionalCommunityMessages.get();
            Set<User> members = communityMessages.getMembers();
            User admin = communityMessages.getAdmin();
            return members.stream()
                    .map(member -> new CommunityMember(
                            member.getId(),
                            member.getName(),
                            member.getEmail(),
                            member.equals(admin)))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    @Transactional
    public CommunityMessages createCommunity(String name, Long adminId, MultipartFile image, String description) throws IOException {
        // Create a new CommunityMessages instance
        CommunityMessages community = new CommunityMessages();

        // Set the community name
        community.setName(name);

        // Fetch the admin user by ID
        User admin = userRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        community.setAdmin(admin);

        // Convert the image (MultipartFile) to byte array and set it
        if (image != null && !image.isEmpty()) {
            byte[] imageBytes = image.getBytes(); // Convert the image file to byte array
            community.setImage(imageBytes);
        }

        // Set the description
        community.setDescription(description);

        // Save the community to the repository and return it
        return communityMessageRepo.save(community);
    }

    public List<CommunityMessages> getCommunityForUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return communityMessageRepo.findAllByMembersContaining(user);
    }

    public List<CommunityMessages> getAllCommunity() {
        return communityMessageRepo.findAll();
    }

}