package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.TeacherDiscussion;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.GeneralDiscussionRepo;
import fusionIQ.AI.V2.fusionIq.repository.TeacherDiscussionRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherDiscussionService {

    @Autowired
    private TeacherDiscussionRepo teacherDiscussionRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private GeneralDiscussionRepo generalDiscussionRepo;

    public List<TeacherDiscussion> getAllTeacherDiscussions() {
        return teacherDiscussionRepo.findAll();
    }

    public Optional<TeacherDiscussion> getTeacherDiscussionById(Long id) {
        return teacherDiscussionRepo.findById(id);
    }


    public TeacherDiscussion createTeacherDiscussion(Long userId, TeacherDiscussion teacherDiscussion) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        teacherDiscussion.setUser(user);

        return teacherDiscussionRepo.save(teacherDiscussion);
    }
    public TeacherDiscussion updateTeacherDiscussion(Long id, TeacherDiscussion updatedTeacherDiscussion) {
        return teacherDiscussionRepo.findById(id).map(teacherDiscussion -> {
            teacherDiscussion.setDiscussionContent(updatedTeacherDiscussion.getDiscussionContent());
            teacherDiscussion.setReplyContent(updatedTeacherDiscussion.getReplyContent());
            teacherDiscussion.setAttachment(updatedTeacherDiscussion.getAttachment());
            return teacherDiscussionRepo.save(teacherDiscussion);
        }).orElseThrow(() -> new RuntimeException("TeacherDiscussion not found"));
    }

    public void deleteTeacherDiscussion(Long id) {
        teacherDiscussionRepo.deleteById(id);
    }
}