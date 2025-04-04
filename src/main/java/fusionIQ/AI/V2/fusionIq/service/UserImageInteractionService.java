package fusionIQ.AI.V2.fusionIq.service;


import fusionIQ.AI.V2.fusionIq.data.ImagePost;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.data.UserImageInteraction;
import fusionIQ.AI.V2.fusionIq.repository.ImagePostRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserImageInteractionRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserImageInteractionService {

    @Autowired
    private UserImageInteractionRepo userImageInteractionRepo;

    @Autowired
    private ImagePostRepo imagePostRepo;
    @Autowired
    private UserRepo userRepo;

    public UserImageInteraction createUserImageInteraction(UserImageInteraction userImageInteraction) {
        return userImageInteractionRepo.save(userImageInteraction);
    }

    public Optional<UserImageInteraction> getUserImageInteractionById(long id) {
        return userImageInteractionRepo.findById(id);
    }

    public List<UserImageInteraction> getUserImageInteractionsByUserId(long userId) {
        return userImageInteractionRepo.findByUserId(userId);
    }

    public List<UserImageInteraction> getUserImageInteractionsByImagePostId(long imagePostId) {
        return userImageInteractionRepo.findByImagePostId(imagePostId);
    }

    public List<UserImageInteraction> getAllUserImageInteractions() {
        return userImageInteractionRepo.findAll();
    }

    public void deleteUserImageInteractionById(long id) {
        userImageInteractionRepo.deleteById(id);
    }

    // Fetch all interactions where imageInteraction == 1
    public List<UserImageInteraction> getAllInteractionsByUserIdAndInteraction(long userId) {
        return userImageInteractionRepo.findByUserIdAndImageInteraction(userId, 1);
    }

    // Fetch interactions where imageInteraction == 1 in the last 7 days
    public List<UserImageInteraction> getRecentInteractionsByUserId(long userId) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return userImageInteractionRepo.findByUserIdAndImageInteractionAndCreatedAtAfter(userId, 1, sevenDaysAgo);
    }

    @Transactional
    public UserImageInteraction createUserImageInteraction(long userId, long imagePostId, long imageInteraction) {
        // Fetch the user
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch the image post
        ImagePost imagePost = imagePostRepo.findById(imagePostId)
                .orElseThrow(() -> new RuntimeException("Image Post not found"));

        // Create new UserImageInteraction
        UserImageInteraction interaction = new UserImageInteraction();
        interaction.setUser(user);
        interaction.setImagePost(imagePost);
        interaction.setImageInteraction(imageInteraction);

        // Save and return
        return userImageInteractionRepo.save(interaction);
    }

    public List<UserImageInteraction> getLast10ImageInteractionsByUserId(long userId) {
        return userImageInteractionRepo.findTop10ByUserIdAndImageInteractionOrderByCreatedAtDesc(userId, 1);
    }

}

