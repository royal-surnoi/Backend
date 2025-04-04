
package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.ChatService;
import fusionIQ.AI.V2.fusionIq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @Autowired
    UserRepo userRepo;

    @Autowired
    private UserService userService;

    @PostMapping("/messages")
    public ResponseEntity<Message> saveMessage(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam String content,
            @RequestParam(required = false) MultipartFile file)
    {
        try {
            User sender = new User();
            sender.setId(senderId);
            User receiver = new User();
            receiver.setId(receiverId);
            Message savedMessage = chatService.saveMessage(sender, receiver, content, file);
            return ResponseEntity.ok(savedMessage);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }


    @GetMapping("/messages/{userId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long userId) {
        try {
            List<Message> messages = chatService.getMessages(userId);
            return ResponseEntity.ok(messages);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/online-status/{userId}")
    public ResponseEntity<Boolean> isUserOnline(@PathVariable Long userId) {
        try {
            boolean isOnline = chatService.isUserOnline(userId);
            return ResponseEntity.ok(isOnline);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/online-status")
    public ResponseEntity<Void> setUserOnline(
            @RequestParam Long userId,
            @RequestParam boolean online) {
        try {
            chatService.setUserOnline(userId, online);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

//    @GetMapping("/conversation")
//    public ResponseEntity<List<Message>> getConversation(
//            @RequestParam Long senderId,
//            @RequestParam Long receiverId) {
//        try {
//            List<Message> conversation = chatService.getConversation(senderId, receiverId);
//            return ResponseEntity.ok(conversation);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body(null);
//        }
//    }

    @GetMapping("/conversation")
    public ResponseEntity<List<Message>> getConversation(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        try {
            List<Message> conversation = chatService.getConversation(senderId, receiverId);
            for (Message message : conversation) {
                if (message.getSender().getId() == senderId.longValue()) {
                    message.setSent(true);
                } else {
                    message.setSent(false);
                }
            }
            return ResponseEntity.ok(conversation);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }

    }

//@GetMapping("/conversation")
//public ResponseEntity<List<Message>> getConversation(
//        @RequestParam Long senderId,
//        @RequestParam Long receiverId) {
//    try {
//        List<Message> conversation = chatService.getConversation(senderId, receiverId);
//
//        for (Message message : conversation) {
//            if (message.getSender().getId() == senderId) {
//                message.setSent(true);
//            } else {
//                message.setSent(false);
//            }
//        }
//
//        return ResponseEntity.ok(conversation);
//
//    } catch (Exception e) {
//        e.printStackTrace();
//        return ResponseEntity.status(500).body(null);
//    }
//}



    @PostMapping("/groups/{groupMessagesId}/addUser")
    public ResponseEntity<GroupMessages> addUserToGroup(
            @PathVariable("groupMessagesId") Long groupMessagesId,
            @RequestParam Long userId) {
        User user = new User(userId);
        GroupMessages group = chatService.addUserToGroup(groupMessagesId, user);
        return ResponseEntity.ok(group);
    }

    @PostMapping("/groups/{groupMessagesId}/removeUser")
    public ResponseEntity<GroupMessages> removeUserFromGroup(
            @PathVariable Long groupMessagesId,
            @RequestParam Long userId) {
        User user = new User(userId);
        GroupMessages group = chatService.removeUserFromGroup(groupMessagesId, user);
        return ResponseEntity.ok(group);
    }




    @PostMapping("/groups/{groupId}/messages")
    public ResponseEntity<Message> sendGroupMessage(
            @PathVariable Long groupId,
            @RequestParam Long senderId,
            @RequestParam String messageContent,
            @RequestParam(required = false) MultipartFile file) {
        try {
            Message message = chatService.saveGroupMessage(groupId, senderId, messageContent, file);
            return ResponseEntity.ok(message);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/groups/{groupId}/messages")
    public ResponseEntity<List<Message>> getGroupMessages(@PathVariable Long groupId) {
        List<Message> messages = chatService.getGroupMessages(groupId);
        return ResponseEntity.ok(messages);
    }


    @GetMapping("groups/{id}/members")
    public ResponseEntity<List<GroupMember>> getGroupMembersWithAdminStatus(@PathVariable Long id) {
        List<GroupMember> memberStatusMap = chatService.getGroupMembersWithAdminStatus(id);
        if (memberStatusMap != null) {
            return ResponseEntity.ok(memberStatusMap);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("groups/isadmin")
    public ResponseEntity<Boolean> isUserAdmin(
            @RequestParam Long groupId,
            @RequestParam Long userId) {
        Long adminId = chatService.isadmin(groupId);
        boolean isAdmin = adminId != null && adminId.equals(userId);
        return ResponseEntity.ok(isAdmin);
    }

    @GetMapping("/groups/{groupMessagesId}")
    public ResponseEntity<GroupMessages> getGroup(@PathVariable Long groupMessagesId) {
        GroupMessages group = chatService.getGroup(groupMessagesId);
        return ResponseEntity.ok(group);
    }

    @PostMapping("/groups")
    public ResponseEntity<GroupMessages> createGroup(
            @RequestParam String name,
            @RequestParam Long adminId,
            @RequestParam List<Long> memberIds) {
        GroupMessages group = chatService.createGroup(name, adminId, memberIds);
        return new ResponseEntity<>(group, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GroupMessages>> getGroupsForUser(@PathVariable Long userId) {
        List<GroupMessages> groups = chatService.getGroupsForUser(userId);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/Group/delete/{groupid}")
    public void deletegroup(@PathVariable Long groupid) {
        chatService.deletegroup(groupid);
    }


    @DeleteMapping("/messages/{senderId}/{messageId}")
    public ResponseEntity<String> deleteMessage(
            @PathVariable Long senderId,
            @PathVariable Long messageId) {
        boolean isDeleted = chatService.deleteMessageBySenderAndMessageId(senderId, messageId);
        if (isDeleted) {
            return ResponseEntity.ok("Message deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Message not found or you do not have permission to delete this message");
        }
    }

//    @DeleteMapping("/chats/{userId}/{otherUserId}")
//    public ResponseEntity<String> deleteChat(
//            @PathVariable Long userId,
//            @PathVariable Long otherUserId) {
//        boolean isDeleted = chatService.deleteChatByUser(userId, otherUserId);
//        if (isDeleted) {
//            return ResponseEntity.ok("Chat deleted successfully");
//        } else {
//            return ResponseEntity.status(404).body("Chat not found or you do not have permission to delete this chat");
//        }
//    }

    @DeleteMapping("/chats/{userId}/{otherUserId}")
    public ResponseEntity<String> deleteChat(
            @PathVariable Long userId,
            @PathVariable Long otherUserId) {
        boolean isDeleted = chatService.deleteChatByUser(userId, otherUserId);
        if (isDeleted) {
            return ResponseEntity.ok("Chat deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Chat not found or you do not have permission to delete this chat");
        }
    }





    @PostMapping("/users/{userId}/status")
    public ResponseEntity<Void> updateUserOnlineStatus(
            @PathVariable Long userId,
            @RequestParam boolean inChatComponent) {
        // Check if user exists and update status accordingly
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (inChatComponent) {
            userService.updateUserOnlineStatus(userId, User.OnlineStatus.ONLINE);
        } else {
            userService.updateUserOnlineStatus(userId, User.OnlineStatus.OFFLINE);
        }

        return ResponseEntity.ok().build();
    }
    @GetMapping("/getUsers/{userId}/status")
    public ResponseEntity<String> getUserOnlineStatus(@PathVariable Long userId) {
        User user = userService.getUserById(userId);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        String statusMessage = user.getOnlineStatus().toString().toLowerCase();
        return ResponseEntity.ok(statusMessage);
    }

    @GetMapping("/notification/unread-count/{userId}")
    public ResponseEntity<Long> getUnreadMessageCount(@PathVariable Long userId) {
        long count = chatService.getUnreadMessageCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/unread/{receiverId}")
    public List<Message> getUnreadMessages(@PathVariable Long receiverId) {
        return chatService.getUnreadMessages(receiverId);
    }

    @PostMapping("/read/{messageId}")
    public void markAsRead(@PathVariable Long messageId) {
        chatService.markAsRead(messageId);
    }

    @PostMapping("/read-all/{userId}")
    public void markAllAsRead(@PathVariable Long userId) {
        chatService.markAllAsRead(userId);
    }
    @GetMapping("/contacts/{userId}")
    public ResponseEntity<List<Message>> getContacts(@PathVariable Long receiverId) {
        List<Message> contacts = chatService.getContactsWithUnreadCounts(receiverId);
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/unread-senders-count/{userId}")
    public ResponseEntity<Long> getUnreadSendersCount(@PathVariable Long userId) {
        long count = chatService.getUnreadSendersCount(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/group/MemberAcceptance/{Userid}/{GroupId}")
    public ResponseEntity<String> acceptGroup(@PathVariable Long Userid, @PathVariable Long GroupId) {
        boolean isAccepted = chatService.acceptGroup(Userid, GroupId); // Assuming this method returns a boolean
        if (isAccepted) {
            return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body("Success");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to accept group");
        }
    }



    @GetMapping("/group/getadmin/{groupId}")
    public ResponseEntity<Long> getadmin(
            @PathVariable Long groupId,
            @RequestParam Long userId) {
        Long adminId = chatService.isadmin(groupId);
//        boolean isAdmin = adminId != null && adminId.equals(userId);
        return ResponseEntity.ok(adminId);
    }

    @GetMapping("/group/IsGroupAccepted/{Userid}/{GroupId}")
    public ResponseEntity<Byte> isGroupAccepted(@PathVariable Long Userid, @PathVariable Long GroupId) {
        byte isAccepted = 0;
        if(chatService.isAccepted(Userid, GroupId)!=null){
            isAccepted = chatService.isAccepted(Userid, GroupId);
        }
        else{
            isAccepted = 0;
        }
        return ResponseEntity.ok(isAccepted);
    }

    @PostMapping("/setReaction")
    public ResponseEntity<Void> setReaction(@RequestParam String type,@RequestParam Long MessageId, @RequestParam String Reaction ){
        return chatService.setReactions(type,MessageId,Reaction);
    }

    @PostMapping("/removeReaction")
    public ResponseEntity<Void> removeReaction(@RequestParam String type,@RequestParam Long MessageId ){
        return chatService.removeReactions(type,MessageId);
    }

    //    Community


    @PostMapping("/Community/{CommunityMessagesId}/addUser")
    public ResponseEntity<CommunityMessages> addUserToCommunity(
            @PathVariable("CommunityMessagesId") Long CommunityMessagesId,
            @RequestParam Long userId) {
        User user = new User(userId);
        CommunityMessages group = chatService.addUserToCommunity(CommunityMessagesId, user);
        return ResponseEntity.ok(group);
    }

    @PostMapping("/Community/{CommunityMessagesId}/removeUser")
    public ResponseEntity<CommunityMessages> removeUserFromCommunity(
            @PathVariable Long CommunityMessagesId,
            @RequestParam Long userId) {
        User user = new User(userId);
        CommunityMessages Community = chatService.removeUserFromCommunity(CommunityMessagesId, user);
        return ResponseEntity.ok(Community);
    }




    @PostMapping("/Community/{CommunityId}/messages")
    public ResponseEntity<Message> sendCommunityMessage(
            @PathVariable Long CommunityId,
            @RequestParam Long senderId,
            @RequestParam String messageContent,
            @RequestParam(required = false) MultipartFile file) {
        try {
            Message message = chatService.saveCommunityMessage(CommunityId, senderId, messageContent, file);
            return ResponseEntity.ok(message);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/Community/{CommunityId}/messages")
    public ResponseEntity<List<Message>> getCommunityMessages(@PathVariable Long CommunityId) {
        List<Message> messages = chatService.getCommunityMessages(CommunityId);
        return ResponseEntity.ok(messages);
    }


    @GetMapping("Community/{id}/members")
    public ResponseEntity<List<CommunityMember>> getCommunityMembersWithAdminStatus(@PathVariable Long id) {
        List<CommunityMember> memberStatusMap = chatService.getCommunityMembersWithAdminStatus(id);
        if (memberStatusMap != null) {
            return ResponseEntity.ok(memberStatusMap);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/Community")
    public ResponseEntity<CommunityMessages> createCommunity(
            @RequestParam String name,
            @RequestParam Long adminId,
            @RequestParam MultipartFile image,  // Accept image as MultipartFile
            @RequestParam String description) {  // Accept description

        // Call the service method to create the community
        try {
            CommunityMessages community = chatService.createCommunity(name, adminId, image, description);
            User user = new User(adminId);
            CommunityMessages group = chatService.addUserToCommunity(community.getId(), user);
            return new ResponseEntity<>(community, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/Community/user/{userId}")
    public ResponseEntity<List<CommunityMessages>> getCommunityForUser(@PathVariable Long userId) {
        List<CommunityMessages> groups = chatService.getCommunityForUser(userId);
        return ResponseEntity.ok(groups);
    }


    @GetMapping("/GetALlCommunity")
    public ResponseEntity<List<CommunityMessages>> getAllCommunity() {
        List<CommunityMessages> Community = chatService.getAllCommunity();
        return ResponseEntity.ok(Community);
    }

}

