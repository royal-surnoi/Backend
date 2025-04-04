
package fusionIQ.AI.V2.fusionIq.testservice;
import fusionIQ.AI.V2.fusionIq.data.Activity;
import fusionIQ.AI.V2.fusionIq.data.Lesson;
import fusionIQ.AI.V2.fusionIq.data.Submission;
import fusionIQ.AI.V2.fusionIq.repository.ActivityRepo;
import fusionIQ.AI.V2.fusionIq.repository.LessonRepo;
import fusionIQ.AI.V2.fusionIq.repository.SubmissionRepo;
import fusionIQ.AI.V2.fusionIq.service.ActivityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityServiceTest {

    @Mock
    private ActivityRepo activityRepo;

    @Mock
    private LessonRepo lessonRepo;

    @Mock
    private SubmissionRepo submissionRepo;

    @InjectMocks
    private ActivityService activityService;

    @Test
    public void testSaveActivity_Success() {
        Lesson lesson = new Lesson();
        Activity activity = new Activity();
        activity.setActivityTitle("Test Activity");

        when(lessonRepo.findById(1L)).thenReturn(Optional.of(lesson));
        when(activityRepo.save(activity)).thenReturn(activity);

        Activity savedActivity = activityService.saveActivity(activity, 1L);

        assertEquals(activity, savedActivity);
        assertEquals(lesson, savedActivity.getLesson());
    }

    @Test
    public void testSaveActivity_LessonNotFound() {
        Activity activity = new Activity();

        when(lessonRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            activityService.saveActivity(activity, 1L);
        });
    }

    @Test
    public void testFindActivityById_Found() {
        Activity activity = new Activity();
        when(activityRepo.findById(1L)).thenReturn(Optional.of(activity));

        Optional<Activity> foundActivity = activityService.findActivityById(1L);

        assertEquals(Optional.of(activity), foundActivity);
    }

    @Test
    public void testFindActivityById_NotFound() {
        when(activityRepo.findById(1L)).thenReturn(Optional.empty());

        Optional<Activity> foundActivity = activityService.findActivityById(1L);

        assertEquals(Optional.empty(), foundActivity);
    }

    @Test
    public void testFindAllActivities() {
        Activity activity1 = new Activity();
        Activity activity2 = new Activity();
        List<Activity> activities = Arrays.asList(activity1, activity2);

        when(activityRepo.findAll()).thenReturn(activities);

        List<Activity> allActivities = activityService.findAllActivities();

        assertEquals(activities, allActivities);
    }

    @Test
    public void testDeleteActivity() {
        doNothing().when(submissionRepo).deleteByActivityId(1L);
        doNothing().when(activityRepo).deleteById(1L);

        activityService.deleteActivity(1L);

        verify(submissionRepo, times(1)).deleteByActivityId(1L);
        verify(activityRepo, times(1)).deleteById(1L);
    }

    @Test
    public void testFindActivitiesByLesson() {
        Activity activity1 = new Activity();
        Activity activity2 = new Activity();
        List<Activity> activities = Arrays.asList(activity1, activity2);

        when(activityRepo.findByLessonId(1L)).thenReturn(activities);

        List<Activity> foundActivities = activityService.findActivitiesByLesson(1L);

        assertEquals(activities, foundActivities);
    }

    @Test
    public void testGetActivitySubmissions() {
        Submission submission1 = new Submission();
        Submission submission2 = new Submission();
        List<Submission> submissions = Arrays.asList(submission1, submission2);

        when(submissionRepo.findByActivityId(1L)).thenReturn(submissions);

        List<Submission> foundSubmissions = activityService.getActivitySubmissions(1L);

        assertEquals(submissions, foundSubmissions);
    }

    @Test
    public void testSavingCourse() {
        Activity activity = new Activity();
        when(activityRepo.save(activity)).thenReturn(activity);

        Activity savedActivity = activityService.savingCourse(activity);

        assertEquals(activity, savedActivity);
    }
}
