package fusionIQ.AI.V2.fusionIq.controller;


import fusionIQ.AI.V2.fusionIq.data.CourseMessage;
import fusionIQ.AI.V2.fusionIq.repository.CourseMessageRepo;
import fusionIQ.AI.V2.fusionIq.service.CourseMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseMessageController {

    @Autowired
    CourseMessageRepo courseMessageRepo;

    @Autowired
    CourseMessageService courseMessageService;


    @PostMapping("/messages/course/{courseId}")
    public ResponseEntity<CourseMessage> sendCourseMessage(
            @PathVariable Long courseId,
            @RequestParam Long fromUserId,
            @RequestParam Long toUserId,
            @RequestParam String subject,
            @RequestParam String message) {
        try {
            CourseMessage courseMessage = courseMessageService.sendCourseMessage(courseId,fromUserId, toUserId, subject, message);
            return new ResponseEntity<>(courseMessage, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/messages/course/{courseId}")
    public ResponseEntity<List<CourseMessage>> getMessagesByCourseId(@PathVariable Long courseId) {
        try {
            List<CourseMessage> messages = courseMessageService.getMessagesByCourseId(courseId);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/messages")
    public ResponseEntity<List<CourseMessage>> getAllMessages() {
        try {
            List<CourseMessage> messages = courseMessageService.getAllMessages();
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
