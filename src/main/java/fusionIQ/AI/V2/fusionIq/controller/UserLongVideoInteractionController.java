package fusionIQ.AI.V2.fusionIq.controller;


import fusionIQ.AI.V2.fusionIq.data.UserLongVideoInteraction;
import fusionIQ.AI.V2.fusionIq.service.UserLongVideoInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userlongvideointeractions")
public class UserLongVideoInteractionController {

    @Autowired
    private UserLongVideoInteractionService userLongVideoInteractionService;

//    private final UserLongVideoInteractionService service;
//
//    public UserLongVideoInteractionController(UserLongVideoInteractionService service) {
//        this.service = service;
//    }

    // Fetch all interactions where longVideoInteraction == 1
    @GetMapping("interactions/{userId}")
    public List<UserLongVideoInteraction> getAllInteractions(@PathVariable long userId) {
        return userLongVideoInteractionService.getAllInteractionsByUserIdAndInteraction(userId);
    }

    // Fetch interactions where longVideoInteraction == 1 in the last 7 days
    @GetMapping("last7daysinteractions/{userId}")
    public List<UserLongVideoInteraction> getRecentInteractions(@PathVariable long userId) {
        return userLongVideoInteractionService.getRecentInteractionsByUserId(userId);
    }

    @PostMapping("/insert/longvideorecommendations")
    public UserLongVideoInteraction createInteraction(
            @RequestParam Long userId,
            @RequestParam Long videoId,
            @RequestParam(required = false) Long interaction) {
        return userLongVideoInteractionService.createInteraction(userId, videoId, interaction);
    }

}
