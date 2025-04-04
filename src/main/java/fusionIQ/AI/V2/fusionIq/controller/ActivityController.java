package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Activity;
import fusionIQ.AI.V2.fusionIq.data.Submission;
import fusionIQ.AI.V2.fusionIq.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping("/save/{lessonId}")
    public ResponseEntity<Activity> createActivity(@RequestBody Activity activity, @PathVariable Long lessonId) {
        try {
            Activity savedActivity = activityService.saveActivity(activity, lessonId);
            return ResponseEntity.ok(savedActivity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/getBy/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        Optional<Activity> activityOpt = activityService.findActivityById(id);
        return activityOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/allActivities")
    public ResponseEntity<List<Activity>> getAllActivities() {
        List<Activity> activities = activityService.findAllActivities();
        return ResponseEntity.ok(activities);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<Activity>> getActivitiesByLesson(@PathVariable Long lessonId) {
        List<Activity> activities = activityService.findActivitiesByLesson(lessonId);
        return ResponseEntity.ok(activities);
    }
    @GetMapping("/{activityId}/submissions")
    public ResponseEntity<List<Submission>> getActivitySubmissions(@PathVariable Long activityId) {
        List<Submission> submissions = activityService.getActivitySubmissions(activityId);
        return ResponseEntity.ok(submissions);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable("id") long id, @RequestBody Activity updatedActivity) {
        Optional<Activity> existingActivityOptional = activityService.getActivityById(id);
        if (existingActivityOptional.isPresent()) {
            Activity existingActivity = existingActivityOptional.get();
            if (updatedActivity.getActivityTitle() != null) {
                existingActivity.setActivityTitle(updatedActivity.getActivityTitle());
            }
            if (updatedActivity.getActivityTitle() != null) {
                existingActivity.setActivityType(updatedActivity.getActivityType());
            }
            if (updatedActivity.getActivityContent() != null) {
                existingActivity.setActivityContent(updatedActivity.getActivityContent());
            }

            Activity savedCourse = activityService.savingCourse(existingActivity);
            return ResponseEntity.ok(savedCourse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
