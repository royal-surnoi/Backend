package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.UserImageInteraction;
import fusionIQ.AI.V2.fusionIq.service.UserImageInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/userImageInteractions")
public class UserImageInteractionController {

    @Autowired
    private UserImageInteractionService userImageInteractionService;

    @PostMapping("/{userId}/{imagePostId}")
    public ResponseEntity<UserImageInteraction> createUserImageInteraction(@RequestBody UserImageInteraction userImageInteraction) {
        UserImageInteraction createdInteraction = userImageInteractionService.createUserImageInteraction(userImageInteraction);
        return ResponseEntity.ok(createdInteraction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserImageInteraction> getUserImageInteractionById(@PathVariable long id) {
        Optional<UserImageInteraction> interaction = userImageInteractionService.getUserImageInteractionById(id);
        return interaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserImageInteraction>> getUserImageInteractionsByUserId(@PathVariable long userId) {
        List<UserImageInteraction> interactions = userImageInteractionService.getUserImageInteractionsByUserId(userId);
        return ResponseEntity.ok(interactions);
    }

    @GetMapping("/imagePost/{imagePostId}")
    public ResponseEntity<List<UserImageInteraction>> getUserImageInteractionsByImagePostId(@PathVariable long imagePostId) {
        List<UserImageInteraction> interactions = userImageInteractionService.getUserImageInteractionsByImagePostId(imagePostId);
        return ResponseEntity.ok(interactions);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserImageInteraction>> getAllUserImageInteractions() {
        List<UserImageInteraction> interactions = userImageInteractionService.getAllUserImageInteractions();
        return ResponseEntity.ok(interactions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserImageInteractionById(@PathVariable long id) {
        userImageInteractionService.deleteUserImageInteractionById(id);
        return ResponseEntity.noContent().build();
    }

    // Fetch all interactions where imageInteraction == 1
    @GetMapping("interactions/{userId}")
    public List<UserImageInteraction> getAllInteractions(@PathVariable long userId) {
        return userImageInteractionService.getAllInteractionsByUserIdAndInteraction(userId);
    }

    // Fetch interactions where imageInteraction == 1 in the last 7 days
    @GetMapping("last7daysinteractions/{userId}")
    public List<UserImageInteraction> getRecentInteractions(@PathVariable long userId) {
        return userImageInteractionService.getRecentInteractionsByUserId(userId);
    }

    @PostMapping("/image")
    public ResponseEntity<UserImageInteraction> createUserImageInteraction(
            @RequestParam long userId,
            @RequestParam long imagePostId,
            @RequestParam long imageInteraction) {
        UserImageInteraction savedInteraction = userImageInteractionService
                .createUserImageInteraction(userId, imagePostId, imageInteraction);
        return new ResponseEntity<>(savedInteraction, HttpStatus.CREATED);
    }

    @GetMapping("last-10-imageInteractions/{userId}")
    public List<UserImageInteraction> getLast10ImageInteractions(@PathVariable long userId) {
        return userImageInteractionService.getLast10ImageInteractionsByUserId(userId);
    }

}
