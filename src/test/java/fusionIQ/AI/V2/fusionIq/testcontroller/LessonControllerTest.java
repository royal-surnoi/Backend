package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.LessonController;
import fusionIQ.AI.V2.fusionIq.data.Activity;
import fusionIQ.AI.V2.fusionIq.data.Lesson;
import fusionIQ.AI.V2.fusionIq.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LessonControllerTest {

    @Mock
    private LessonService lessonService;

    @InjectMocks
    private LessonController lessonController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateLesson_Success() {
        Long courseId = 1L;
        Lesson lesson = new Lesson();
        Lesson savedLesson = new Lesson();

        when(lessonService.saveLesson(lesson, courseId)).thenReturn(savedLesson);

        ResponseEntity<Lesson> response = lessonController.createLesson(lesson, courseId);

        assertEquals(ResponseEntity.ok(savedLesson), response);
        verify(lessonService, times(1)).saveLesson(lesson, courseId);
    }

    @Test
    void testGetLessonById_Success() {
        Long lessonId = 1L;
        Lesson lesson = new Lesson();

        when(lessonService.findLessonById(lessonId)).thenReturn(Optional.of(lesson));

        ResponseEntity<Lesson> response = lessonController.getLessonById(lessonId);

        assertEquals(ResponseEntity.ok(lesson), response);
        verify(lessonService, times(1)).findLessonById(lessonId);
    }

    @Test
    void testGetAllLessons_Success() {
        List<Lesson> lessons = Arrays.asList(new Lesson(), new Lesson());

        when(lessonService.findAllLessons()).thenReturn(lessons);

        ResponseEntity<List<Lesson>> response = lessonController.getAllLessons();

        assertEquals(ResponseEntity.ok(lessons), response);
        verify(lessonService, times(1)).findAllLessons();
    }

    @Test
    void testDeleteLesson_Success() {
        Long lessonId = 1L;

        doNothing().when(lessonService).deleteLesson(lessonId);

        ResponseEntity<Void> response = lessonController.deleteLesson(lessonId);

        assertEquals(ResponseEntity.noContent().build(), response);
        verify(lessonService, times(1)).deleteLesson(lessonId);
    }

    @Test
    void testGetLessonsByCourse_Success() {
        Long courseId = 1L;
        List<Lesson> lessons = Arrays.asList(new Lesson(), new Lesson());

        when(lessonService.findLessonsByCourse(courseId)).thenReturn(lessons);

        ResponseEntity<List<Lesson>> response = lessonController.getLessonsByCourse(courseId);

        assertEquals(ResponseEntity.ok(lessons), response);
        verify(lessonService, times(1)).findLessonsByCourse(courseId);
    }

    @Test
    void testGetLessonActivities_Success() {
        Long lessonId = 1L;
        List<Activity> activities = Arrays.asList(new Activity(), new Activity());

        when(lessonService.getLessonActivities(lessonId)).thenReturn(activities);

        ResponseEntity<List<Activity>> response = lessonController.getLessonActivities(lessonId);

        assertEquals(ResponseEntity.ok(activities), response);
        verify(lessonService, times(1)).getLessonActivities(lessonId);
    }

    @Test
    void testUpdateLesson_Success() {
        Long lessonId = 1L;
        Lesson updatedLesson = new Lesson();
        Lesson existingLesson = new Lesson();

        when(lessonService.getLessonById(lessonId)).thenReturn(Optional.of(existingLesson));
        when(lessonService.savingLesson(existingLesson)).thenReturn(existingLesson);

        ResponseEntity<Lesson> response = lessonController.updateLesson(lessonId, updatedLesson);

        assertEquals(ResponseEntity.ok(existingLesson), response);
        verify(lessonService, times(1)).getLessonById(lessonId);
        verify(lessonService, times(1)).savingLesson(existingLesson);
    }

    @Test
    void testPatchLessonByCourseId_Success() {
        Long courseId = 1L;
        Map<String, Object> updates = Map.of("lessonTitle", "Updated Title");
        Lesson updatedLesson = new Lesson();

        when(lessonService.patchLessonByCourseId(courseId, updates)).thenReturn(updatedLesson);

        ResponseEntity<Lesson> response = lessonController.patchLessonByCourseId(courseId, updates);

        assertEquals(ResponseEntity.ok(updatedLesson), response);
        verify(lessonService, times(1)).patchLessonByCourseId(courseId, updates);
    }

    @Test
    void testCreateLessonModule_Success() {
        Long lessonModuleId = 1L;
        Lesson lesson = new Lesson();
        Lesson savedLesson = new Lesson();

        when(lessonService.savedLesson(lesson, lessonModuleId)).thenReturn(savedLesson);

        ResponseEntity<Lesson> response = lessonController.createLessonModule(lesson, lessonModuleId);

        assertEquals(ResponseEntity.ok(savedLesson), response);
        verify(lessonService, times(1)).savedLesson(lesson, lessonModuleId);
    }
}
