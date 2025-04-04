package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.UserShortVideoInteraction;
import fusionIQ.AI.V2.fusionIq.service.UserShortVideoInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usershortvideointeractions")
public class UserShortVideoInteractionController {

    @Autowired
    private UserShortVideoInteractionService userShortVideoInteractionService;

    @PostMapping("/shortvideointeraction")
    public ResponseEntity<UserShortVideoInteraction> postInteraction(
            @RequestParam long userId,
            @RequestParam long shortVideoId,
            @RequestParam long shortVideoInteraction) {
        UserShortVideoInteraction interaction = userShortVideoInteractionService.post(userId, shortVideoId, shortVideoInteraction);
        return ResponseEntity.ok(interaction);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserShortVideoInteraction>> getByUserId(@PathVariable long userId) {
        List<UserShortVideoInteraction> interactions = userShortVideoInteractionService.getByUserId(userId);
        return ResponseEntity.ok(interactions);
    }

    @GetMapping("/short-video/{shortVideoId}")
    public ResponseEntity<List<UserShortVideoInteraction>> getByShortVideoId(@PathVariable long shortVideoId) {
        List<UserShortVideoInteraction> interactions = userShortVideoInteractionService.getByShortVideoId(shortVideoId);
        return ResponseEntity.ok(interactions);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserShortVideoInteraction>> getAll() {
        List<UserShortVideoInteraction> interactions = userShortVideoInteractionService.getAll();
        return ResponseEntity.ok(interactions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        userShortVideoInteractionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("interactions/{userId}")
    public List<UserShortVideoInteraction> getUserInteractions(
            @PathVariable long userId) {
        return userShortVideoInteractionService.getInteractionsByUserAndInteraction(userId);
    }

    @GetMapping("last7daysinteractions/{userId}")
    public List<UserShortVideoInteraction> getRecentUserInteractions(@PathVariable long userId) {
        return userShortVideoInteractionService.getInteractionsByUserAndRecentInteraction(userId);
    }
}
