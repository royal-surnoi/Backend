package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.CourseDiscussion;
import fusionIQ.AI.V2.fusionIq.service.CourseDiscussionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/courseDiscussion")
public class CourseDiscussionController {

    @Autowired
    private CourseDiscussionService courseDiscussionService;

    @GetMapping("/getAll")
    public List<CourseDiscussion> getAllCourseDiscussions() {
        return courseDiscussionService.getAllCourseDiscussions();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CourseDiscussion> getCourseDiscussionById(@PathVariable Long id) {
        return courseDiscussionService.getCourseDiscussionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{courseId}/{userId}")
    public CourseDiscussion createCourseDiscussion(
            @PathVariable Long courseId,
            @PathVariable Long userId,
            @RequestParam(value = "discussionContent", required = false) String discussionContent,
            @RequestParam(value = "replyContent", required = false) String replyContent,
            @RequestParam(value = "attachment", required = false) MultipartFile attachment) throws IOException {

        CourseDiscussion courseDiscussion = new CourseDiscussion();
        courseDiscussion.setDiscussionContent(discussionContent);
        courseDiscussion.setReplyContent(replyContent);

        if (attachment != null && !attachment.isEmpty()) {
            courseDiscussion.setAttachment(attachment.getBytes());
        }

        return courseDiscussionService.createCourseDiscussion(courseId, userId, courseDiscussion);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CourseDiscussion> updateCourseDiscussion(@PathVariable Long id, @RequestBody CourseDiscussion updatedCourseDiscussion) {
        return ResponseEntity.ok(courseDiscussionService.updateCourseDiscussion(id, updatedCourseDiscussion));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCourseDiscussion(@PathVariable Long id) {
        courseDiscussionService.deleteCourseDiscussion(id);
        return ResponseEntity.noContent().build();
    }
}
