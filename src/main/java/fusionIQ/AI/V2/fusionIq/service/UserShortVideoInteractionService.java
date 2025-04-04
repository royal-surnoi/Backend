package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.ShortVideo;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.UserShortVideoInteraction;
import fusionIQ.AI.V2.fusionIq.repository.UserShortVideoInteractionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserShortVideoInteractionService {

    @Autowired
    private UserShortVideoInteractionRepo userShortVideoInteractionRepo;

    public UserShortVideoInteraction post(long userId, long shortVideoId, long shortVideoInteraction) {
        User user = new User();
        user.setId(userId);

        ShortVideo shortVideo = new ShortVideo();
        shortVideo.setId(shortVideoId);

        UserShortVideoInteraction interaction = new UserShortVideoInteraction();
        interaction.setUser(user);
        interaction.setShortVideo(shortVideo);
        interaction.setShortVideoInteraction(shortVideoInteraction);

        return userShortVideoInteractionRepo.save(interaction);
    }

    public List<UserShortVideoInteraction> getByUserId(long userId) {
        return userShortVideoInteractionRepo.findByUserId(userId);
    }

    public List<UserShortVideoInteraction> getByShortVideoId(long shortVideoId) {
        return userShortVideoInteractionRepo.findByShortVideoId(shortVideoId);
    }

    public List<UserShortVideoInteraction> getAll() {
        return userShortVideoInteractionRepo.findAll();
    }

    public void deleteById(long id) {
        userShortVideoInteractionRepo.deleteById(id);
    }

    public List<UserShortVideoInteraction> getInteractionsByUserAndInteraction(long userId) {
        return userShortVideoInteractionRepo.findByUserIdAndShortVideoInteraction(userId, 1);
    }

    public List<UserShortVideoInteraction> getInteractionsByUserAndRecentInteraction(long userId) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return userShortVideoInteractionRepo.findByUserIdAndShortVideoInteractionAndCreatedAtAfter(userId, 1, sevenDaysAgo);
    }
}

