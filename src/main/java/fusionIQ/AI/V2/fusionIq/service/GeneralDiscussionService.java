package fusionIQ.AI.V2.fusionIq.service;


import fusionIQ.AI.V2.fusionIq.data.GeneralDiscussion;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.GeneralDiscussionRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GeneralDiscussionService {

    @Autowired
    private GeneralDiscussionRepo generalDiscussionRepo;

    @Autowired
    private UserRepo userRepo;

    public List<GeneralDiscussion> getAllGeneralDiscussions() {
        return generalDiscussionRepo.findAll();
    }

    public Optional<GeneralDiscussion> getGeneralDiscussionById(Long id) {
        return generalDiscussionRepo.findById(id);
    }


public GeneralDiscussion createGeneralDiscussion(Long userId, GeneralDiscussion generalDiscussion) {
    User user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    generalDiscussion.setUser(user);

    return generalDiscussionRepo.save(generalDiscussion);
}

    public GeneralDiscussion updateGeneralDiscussion(Long id, GeneralDiscussion updatedGeneralDiscussion) {
        return generalDiscussionRepo.findById(id).map(generalDiscussion -> {
            generalDiscussion.setDiscussionContent(updatedGeneralDiscussion.getDiscussionContent());
            generalDiscussion.setReplyContent(updatedGeneralDiscussion.getReplyContent());
            generalDiscussion.setAttachment(updatedGeneralDiscussion.getAttachment());
            return generalDiscussionRepo.save(generalDiscussion);
        }).orElseThrow(() -> new RuntimeException("GeneralDiscussion not found"));
    }

    public void deleteGeneralDiscussion(Long id) {
        generalDiscussionRepo.deleteById(id);
    }
}
