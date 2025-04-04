package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Message;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.MessageRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    MessageRepo messageRepo;


    @GetMapping("/get/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Optional<Message> messageOpt = messageService.findMessageById(id);
        return messageOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.findAllMessages();
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<Message>> getMessagesBySender(@PathVariable Long senderId) {
        List<Message> messages = messageService.findMessagesBySender(senderId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/receiver/{receiverId}")
    public ResponseEntity<List<Message>> getMessagesByReceiver(@PathVariable Long receiverId) {
        List<Message> messages = messageService.findMessagesByReceiver(receiverId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<Message>> getMessagesBetweenUsers(@RequestParam Long senderId, @RequestParam Long receiverId) {
        List<Message> messages = messageService.findMessagesBetweenUsers(senderId, receiverId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/add/{senderId}/{receiverId}")
    public Message addMessage(@PathVariable Long senderId,
                              @PathVariable Long receiverId,
                              @RequestParam String messageContent) {
        Optional<User> sender = userRepo.findById(senderId);
        Optional<User> receiver = userRepo.findById(receiverId);

        if (sender.isPresent() && receiver.isPresent()) {
            Message message = new Message();
            message.setSender(sender.get());
            message.setReceiver(receiver.get());
            message.setMessageContent(messageContent);
            message.setCreatedAt(LocalDateTime.now());
            return messageRepo.save(message);
        } else {
            throw new RuntimeException("Sender or Receiver not found");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable("id") Long id,
                                                 @RequestParam(required = false) Long senderId,
                                                 @RequestParam(required = false) Long receiverId,
                                                 @RequestParam String messageContent) {
        Optional<Message> messageOpt = messageRepo.findById(id);
        if (messageOpt.isPresent()) {
            Message message = messageOpt.get();

            if (senderId != null) {
                Optional<User> sender = userRepo.findById(senderId);
                if (sender.isPresent()) {
                    message.setSender(sender.get());
                } else {
                    throw new RuntimeException("Sender not found");
                }
            }


            if (receiverId != null) {
                Optional<User> receiver = userRepo.findById(receiverId);
                if (receiver.isPresent()) {
                    message.setReceiver(receiver.get());
                } else {
                    throw new RuntimeException("Receiver not found");
                }
            }

            message.setMessageContent(messageContent);
            message.setCreatedAt(LocalDateTime.now()); // update timestamp if needed
            Message updatedMessage = messageRepo.save(message);
            return ResponseEntity.ok(updatedMessage);
        } else {
            return ResponseEntity.notFound().build();
        }


    }




}

