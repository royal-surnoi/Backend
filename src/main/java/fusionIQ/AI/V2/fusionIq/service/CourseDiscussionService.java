package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.CourseDiscussion;

import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.CourseDiscussionRepo;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseDiscussionService {

    @Autowired
    private CourseDiscussionRepo courseDiscussionRepo;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private UserRepo userRepo;

    public List<CourseDiscussion> getAllCourseDiscussions() {
        return courseDiscussionRepo.findAll();
    }

    public Optional<CourseDiscussion> getCourseDiscussionById(Long id) {
        return courseDiscussionRepo.findById(id);
    }


public CourseDiscussion createCourseDiscussion(Long courseId, Long userId, CourseDiscussion courseDiscussion) {
    Course course = courseRepo.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));
    User user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

    courseDiscussion.setCourse(course);
    courseDiscussion.setUser(user);

    return courseDiscussionRepo.save(courseDiscussion);
}


    public CourseDiscussion updateCourseDiscussion(Long id, CourseDiscussion updatedCourseDiscussion) {
        return courseDiscussionRepo.findById(id).map(courseDiscussion -> {
            courseDiscussion.setDiscussionContent(updatedCourseDiscussion.getDiscussionContent());
            courseDiscussion.setReplyContent(updatedCourseDiscussion.getReplyContent());
            courseDiscussion.setAttachment(updatedCourseDiscussion.getAttachment());
            return courseDiscussionRepo.save(courseDiscussion);
        }).orElseThrow(() -> new RuntimeException("CourseDiscussion not found"));
    }

    public void deleteCourseDiscussion(Long id) {
        courseDiscussionRepo.deleteById(id);
    }
}
