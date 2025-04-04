package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.TeacherDiscussion;
import fusionIQ.AI.V2.fusionIq.service.TeacherDiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/teacherDiscussion")
public class TeacherDiscussionController {

    @Autowired
    private TeacherDiscussionService teacherDiscussionService;

    @GetMapping("/getAll")
    public List<TeacherDiscussion> getAllTeacherDiscussions() {
        return teacherDiscussionService.getAllTeacherDiscussions();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TeacherDiscussion> getTeacherDiscussionById(@PathVariable Long id) {
        return teacherDiscussionService.getTeacherDiscussionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

@PostMapping("/{userId}")
public TeacherDiscussion createTeacherDiscussion(
        @PathVariable Long userId,
        @RequestParam(value = "discussionContent", required = false) String discussionContent,
        @RequestParam(value = "replyContent", required = false) String replyContent,
        @RequestParam(value = "attachment", required = false) MultipartFile attachment) throws IOException {

    TeacherDiscussion teacherDiscussion = new TeacherDiscussion();
    teacherDiscussion.setDiscussionContent(discussionContent);
    teacherDiscussion.setReplyContent(replyContent);

    if (attachment != null && !attachment.isEmpty()) {
        teacherDiscussion.setAttachment(attachment.getBytes());
    }

    return teacherDiscussionService.createTeacherDiscussion(userId, teacherDiscussion);
}

    @PutMapping("/update/{id}")
    public ResponseEntity<TeacherDiscussion> updateTeacherDiscussion(@PathVariable Long id, @RequestBody TeacherDiscussion updatedTeacherDiscussion) {
        return ResponseEntity.ok(teacherDiscussionService.updateTeacherDiscussion(id, updatedTeacherDiscussion));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTeacherDiscussion(@PathVariable Long id) {
        teacherDiscussionService.deleteTeacherDiscussion(id);
        return ResponseEntity.noContent().build();
    }
}
