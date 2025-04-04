package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.GeneralDiscussion;
import fusionIQ.AI.V2.fusionIq.service.GeneralDiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/generalDiscussion")
public class GeneralDiscussionController {

    @Autowired
    private GeneralDiscussionService generalDiscussionService;

    @GetMapping("/getAll")
    public List<GeneralDiscussion> getAllGeneralDiscussions() {
        return generalDiscussionService.getAllGeneralDiscussions();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<GeneralDiscussion> getGeneralDiscussionById(@PathVariable Long id) {
        return generalDiscussionService.getGeneralDiscussionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{userId}")
    public GeneralDiscussion createGeneralDiscussion(
            @PathVariable Long userId,
            @RequestParam(value = "discussionContent", required = false) String discussionContent,
            @RequestParam(value = "replyContent", required = false) String replyContent,
            @RequestParam(value = "attachment", required = false) MultipartFile attachment) throws IOException {

        GeneralDiscussion generalDiscussion = new GeneralDiscussion();
        generalDiscussion.setDiscussionContent(discussionContent);
        generalDiscussion.setReplyContent(replyContent);

        if (attachment != null && !attachment.isEmpty()) {
            generalDiscussion.setAttachment(attachment.getBytes());
        }

        return generalDiscussionService.createGeneralDiscussion(userId, generalDiscussion);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<GeneralDiscussion> updateGeneralDiscussion(@PathVariable Long id, @RequestBody GeneralDiscussion updatedGeneralDiscussion) {
        return ResponseEntity.ok(generalDiscussionService.updateGeneralDiscussion(id, updatedGeneralDiscussion));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGeneralDiscussion(@PathVariable Long id) {
        generalDiscussionService.deleteGeneralDiscussion(id);
        return ResponseEntity.noContent().build();
    }
}
