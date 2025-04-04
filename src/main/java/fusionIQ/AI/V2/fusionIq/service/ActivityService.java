package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Activity;
import fusionIQ.AI.V2.fusionIq.data.Lesson;
import fusionIQ.AI.V2.fusionIq.data.Submission;
import fusionIQ.AI.V2.fusionIq.repository.ActivityRepo;
import fusionIQ.AI.V2.fusionIq.repository.LessonRepo;
import fusionIQ.AI.V2.fusionIq.repository.SubmissionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepo activityRepo;
    @Autowired
    private LessonRepo lessonRepo;
    @Autowired
    private SubmissionRepo submissionRepo;

    public Activity saveActivity(Activity activity, Long lessonId) {
        Optional<Lesson> lessonOpt = lessonRepo.findById(lessonId);
        if (lessonOpt.isPresent()) {
            activity.setLesson(lessonOpt.get());
        } else {
            throw new IllegalArgumentException("Lesson not found");
        }
        return activityRepo.save(activity);
    }

    public Optional<Activity> findActivityById(Long id) {
        return activityRepo.findById(id);
    }

    public List<Activity> findAllActivities() {
        return activityRepo.findAll();
    }

    public void deleteActivity(Long id) {
        submissionRepo.deleteByActivityId(id);
        activityRepo.deleteById(id);
    }

    public List<Activity> findActivitiesByLesson(Long lessonId) {
        return activityRepo.findByLessonId(lessonId);
    }

    public List<Submission> getActivitySubmissions(Long activityId) {
        return submissionRepo.findByActivityId(activityId);
    }

    public Optional<Activity> getActivityById(long id) {
        return activityRepo.findById(id);
    }

    public Activity savingCourse(Activity existingActivity) {
        return activityRepo.save(existingActivity);
    }

}
