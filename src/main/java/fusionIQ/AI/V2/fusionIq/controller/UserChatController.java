package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.service.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserChatController {

    @Autowired
    private UserChatService userChatService;

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestParam int id, @RequestParam String userQuery) {
        try {
            String response = userChatService.handleUserQuery(id, userQuery);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }
}
