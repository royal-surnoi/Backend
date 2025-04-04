package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.UserArticleInteraction;
import fusionIQ.AI.V2.fusionIq.service.UserArticleInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userArticleInteractions")
public class UserArticleInteractionController {

    @Autowired
    private UserArticleInteractionService userArticleInteractionService;

    @PostMapping("/post/{userId}/{articleId}")
    public UserArticleInteraction postByUserIdAndArticleId(
            @PathVariable Long userId,
            @PathVariable Long articleId,
            @RequestParam Long articleInteraction) {
        return userArticleInteractionService.postByUserIdAndArticleId(userId, articleId, articleInteraction);
    }


    @GetMapping("/user/{userId}")
    public List<UserArticleInteraction> getByUserId(@PathVariable Long userId) {
        return userArticleInteractionService.getByUserId(userId);
    }

    @GetMapping("/article/{articleId}")
    public List<UserArticleInteraction> getByArticleId(@PathVariable Long articleId) {
        return userArticleInteractionService.getByArticleId(articleId);
    }

    @GetMapping("/all")
    public List<UserArticleInteraction> getAll() {
        return userArticleInteractionService.getAll();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        userArticleInteractionService.deleteById(id);
    }

    // Fetch all interactions where articleInteraction == 1
    @GetMapping("interactions/{userId}")
    public List<UserArticleInteraction> getAllInteractions(@PathVariable long userId) {
        return userArticleInteractionService.getAllInteractionsByUserIdAndInteraction(userId);
    }

    // Fetch interactions where articleInteraction == 1 in the last 7 days
    @GetMapping("last7daysinteractions/{userId}")
    public List<UserArticleInteraction> getRecentInteractions(@PathVariable long userId) {
        return userArticleInteractionService.getRecentInteractionsByUserId(userId);
    }

    @GetMapping("last-10-articleFeedInteractions/{userId}")
    public List<UserArticleInteraction> getLast10FeedInteractions(@PathVariable long userId) {
        return userArticleInteractionService.getLast10FeedInteractionsByUserId(userId);
    }

}

