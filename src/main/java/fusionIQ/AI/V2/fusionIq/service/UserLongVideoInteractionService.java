package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.LongVideo;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.UserLongVideoInteraction;
import fusionIQ.AI.V2.fusionIq.exception.UserNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.LongVideoRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserLongVideoInteractionRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserLongVideoInteractionService {

    @Autowired
    private UserLongVideoInteractionRepo userLongVideoInteractionRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private LongVideoRepo longVideoRepo;

    // Fetch all interactions where longVideoInteraction == 1
    public List<UserLongVideoInteraction> getAllInteractionsByUserIdAndInteraction(long userId) {
        return userLongVideoInteractionRepo.findByUserIdAndLongVideoInteraction(userId, 1);
    }

    // Fetch interactions where longVideoInteraction == 1 in the last 7 days
    public List<UserLongVideoInteraction> getRecentInteractionsByUserId(long userId) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return userLongVideoInteractionRepo.findByUserIdAndLongVideoInteractionAndCreatedAtAfter(userId, 1, sevenDaysAgo);
    }

    public UserLongVideoInteraction createInteraction(Long userId, Long videoId, Long interaction) {
        // Fetch User
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Fetch LongVideo
        LongVideo longVideo = longVideoRepo.findById(videoId)
                .orElseThrow(() -> new UserNotFoundException("Long video not found with id: " + videoId));

        // Create new interaction with default interaction value 0 if not provided
        UserLongVideoInteraction userLongVideoInteraction = new UserLongVideoInteraction();
        userLongVideoInteraction.setUser(user);
        userLongVideoInteraction.setLongVideo(longVideo);
        userLongVideoInteraction.setLongVideoInteraction(Optional.ofNullable(interaction).orElse(0L));

        return userLongVideoInteractionRepo.save(userLongVideoInteraction);
    }
}