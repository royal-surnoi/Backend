package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Message;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.MessageRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private UserRepo userRepo;

    public Message saveMessage(Message message, Long senderId, Long receiverId) {
        Optional<User> senderOpt = userRepo.findById(senderId);
        Optional<User> receiverOpt = userRepo.findById(receiverId);

        if (senderOpt.isPresent() && receiverOpt.isPresent()) {
            message.setSender(senderOpt.get());
            message.setReceiver(receiverOpt.get());

            return messageRepo.save(message);
        } else {
            throw new IllegalArgumentException("Sender or Receiver not found");
        }
    }

    public Optional<Message> findMessageById(Long id) {
        return messageRepo.findById(id);
    }

    public List<Message> findAllMessages() {
        return messageRepo.findAll();
    }

    public void deleteMessage(Long id) {
        messageRepo.deleteById(id);
    }

    public List<Message> findMessagesBySender(Long senderId) {
        return messageRepo.findBySenderId(senderId);
    }

    public List<Message> findMessagesByReceiver(Long receiverId) {
        return messageRepo.findByReceiverId(receiverId);
    }

    public List<Message> findMessagesBetweenUsers(Long senderId, Long receiverId) {
        return messageRepo.findBySenderIdAndReceiverId(senderId, receiverId);
    }

    public Message getMessageById(Long id) {
        return messageRepo.findById(id).orElseThrow(() -> new RuntimeException("Message not found"));
    }




}
