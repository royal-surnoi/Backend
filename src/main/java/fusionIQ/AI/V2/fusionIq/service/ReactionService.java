package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Message;
import fusionIQ.AI.V2.fusionIq.data.Reactions;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.ReactionRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

@Service
public class ReactionService {
    @Autowired
    ReactionRepo reactionsRepository;

    public boolean hasUserReacted(Message message, User user) {
        return reactionsRepository.findByMessageAndUser(message, user).isPresent();
    }

    public void saveReaction(Reactions reaction) {
        reactionsRepository.save(reaction);
    }

    public Reactions getReactionByMessageAndUser(Message message, User user) {
        return reactionsRepository.findByMessageAndUser(message, user).orElse(null);
    }

    @Transactional
    public void deleteReaction(Long Messageid, Long Userid) {
        reactionsRepository.deleteData(Messageid,Userid);
    }
}
