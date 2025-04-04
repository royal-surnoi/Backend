package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Message;
import fusionIQ.AI.V2.fusionIq.data.Reactions;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.service.MessageService;
import fusionIQ.AI.V2.fusionIq.service.ReactionService;
import fusionIQ.AI.V2.fusionIq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Reactions")
public class ReactionController {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    ReactionService reactionsService;


    @PostMapping("/react")
    public ResponseEntity<String> reactToMessage(@RequestParam Long messageId,
                                                 @RequestParam Long userId,
                                                 @RequestParam String reaction) {
        try {
            Message message = messageService.getMessageById(messageId);
            User user = userService.getUserById(userId);

            // Check if the user has already reacted to the message
            Reactions existingReaction = reactionsService.getReactionByMessageAndUser(message, user);

            if (existingReaction != null) {
                // If the reaction is the same, remove the reaction (dereact)
                if (existingReaction.getReaction().equals(reaction)) {
                    reactionsService.deleteReaction(messageId,userId);
                    return new ResponseEntity<>("Reaction removed successfully.", HttpStatus.OK);
                } else {
                    // If the reaction is different, update it
                    existingReaction.setReaction(reaction);
                    reactionsService.saveReaction(existingReaction);
                    return new ResponseEntity<>("Reaction updated successfully.", HttpStatus.OK);
                }
            } else {
                // If no reaction exists, create a new one
                Reactions newReaction = new Reactions();
                newReaction.setMessage(message);
                newReaction.setUser(user);
                newReaction.setReaction(reaction);
                reactionsService.saveReaction(newReaction);

                return new ResponseEntity<>("Reaction added successfully.", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing reaction: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/dereact")
    public ResponseEntity<String> removeReaction(@RequestParam Long messageId, @RequestParam Long userId) {
        try {
            Message message = messageService.getMessageById(messageId);
            User user = userService.getUserById(userId);

            Reactions existingReaction = reactionsService.getReactionByMessageAndUser(message, user);
            if (existingReaction == null) {
                return new ResponseEntity<>("No reaction found for the user on this message.", HttpStatus.NOT_FOUND);
            }

            reactionsService.deleteReaction(messageId,userId);
            return new ResponseEntity<>("Reaction removed successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error removing reaction: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
