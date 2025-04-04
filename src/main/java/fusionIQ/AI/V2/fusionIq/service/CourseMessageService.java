package fusionIQ.AI.V2.fusionIq.service;


import fusionIQ.AI.V2.fusionIq.data.CourseMessage;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.CourseMessageRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseMessageService {

    @Autowired
    CourseMessageRepo courseMessageRepo;


    @Autowired
    UserRepo userRepo;

    public CourseMessage sendCourseMessage(Long courseId, Long fromUserId, Long toUserId, String subject, String message) {
        User fromUser = userRepo.findById(fromUserId).orElseThrow(() -> new IllegalArgumentException("Invalid fromUserId"));
        User toUser = userRepo.findById(toUserId).orElseThrow(() -> new IllegalArgumentException("Invalid toUserId"));

        CourseMessage courseMessage = new CourseMessage();
        courseMessage.setFrom(fromUser);
        courseMessage.setTo(toUser);
        courseMessage.setSubject(subject);
        courseMessage.setCourseMessage(message);
        courseMessage.setSentAt(LocalDateTime.now());

        return courseMessageRepo.save(courseMessage);
    }



    public List<CourseMessage> getMessagesByCourseId(Long courseId) {
        return courseMessageRepo.findByCourseId(courseId);
    }


    public List<CourseMessage> getAllMessages() {
        return courseMessageRepo.findAll();
    }

}
