package fusionIQ.AI.V2.fusionIq.testcontroller;
import fusionIQ.AI.V2.fusionIq.controller.ActivityController;
import fusionIQ.AI.V2.fusionIq.data.Activity;
import fusionIQ.AI.V2.fusionIq.data.Submission;
import fusionIQ.AI.V2.fusionIq.service.ActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActivityControllerTest {

    @Mock
    private ActivityService activityService;

    @InjectMocks
    private ActivityController activityController;

    @Test
    public void testCreateActivity_Success() {
        Activity activity = new Activity();
        activity.setActivityTitle("Test Activity");

        when(activityService.saveActivity(any(Activity.class), eq(1L))).thenReturn(activity);

        ResponseEntity<Activity> response = activityController.createActivity(activity, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activity, response.getBody());
    }

    @Test
    public void testCreateActivity_LessonNotFound() {
        Activity activity = new Activity();

        when(activityService.saveActivity(any(Activity.class), eq(1L))).thenThrow(new IllegalArgumentException("Lesson not found"));

        ResponseEntity<Activity> response = activityController.createActivity(activity, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetActivityById_Found() {
        Activity activity = new Activity();
        when(activityService.findActivityById(1L)).thenReturn(Optional.of(activity));

        ResponseEntity<Activity> response = activityController.getActivityById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activity, response.getBody());
    }

    @Test
    public void testGetActivityById_NotFound() {
        when(activityService.findActivityById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Activity> response = activityController.getActivityById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetAllActivities() {
        Activity activity1 = new Activity();
        Activity activity2 = new Activity();
        List<Activity> activities = Arrays.asList(activity1, activity2);

        when(activityService.findAllActivities()).thenReturn(activities);

        ResponseEntity<List<Activity>> response = activityController.getAllActivities();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activities, response.getBody());
    }

    @Test
    public void testDeleteActivity() {
        doNothing().when(activityService).deleteActivity(1L);

        ResponseEntity<Void> response = activityController.deleteActivity(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testGetActivitiesByLesson() {
        Activity activity1 = new Activity();
        Activity activity2 = new Activity();
        List<Activity> activities = Arrays.asList(activity1, activity2);

        when(activityService.findActivitiesByLesson(1L)).thenReturn(activities);

        ResponseEntity<List<Activity>> response = activityController.getActivitiesByLesson(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(activities, response.getBody());
    }

    @Test
    public void testGetActivitySubmissions() {
        Submission submission1 = new Submission();
        Submission submission2 = new Submission();
        List<Submission> submissions = Arrays.asList(submission1, submission2);

        when(activityService.getActivitySubmissions(1L)).thenReturn(submissions);

        ResponseEntity<List<Submission>> response = activityController.getActivitySubmissions(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(submissions, response.getBody());
    }

    @Test
    public void testUpdateActivity_Success() {
        Activity existingActivity = new Activity();
        existingActivity.setId(1L);
        existingActivity.setActivityTitle("Old Title");

        Activity updatedActivity = new Activity();
        updatedActivity.setActivityTitle("New Title");

        when(activityService.getActivityById(1L)).thenReturn(Optional.of(existingActivity));
        when(activityService.savingCourse(any(Activity.class))).thenReturn(updatedActivity);

        ResponseEntity<Activity> response = activityController.updateActivity(1L, updatedActivity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedActivity.getActivityTitle(), response.getBody().getActivityTitle());
    }

    @Test
    public void testUpdateActivity_NotFound() {
        Activity updatedActivity = new Activity();

        when(activityService.getActivityById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Activity> response = activityController.updateActivity(1L, updatedActivity);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
