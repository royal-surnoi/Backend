package fusionIQ.AI.V2.fusionIq.testservice;

import fusionIQ.AI.V2.fusionIq.data.Activity;
import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.Lesson;
import fusionIQ.AI.V2.fusionIq.data.LessonModule;
import fusionIQ.AI.V2.fusionIq.repository.ActivityRepo;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.LessonModuleRepo;
import fusionIQ.AI.V2.fusionIq.repository.LessonRepo;
import fusionIQ.AI.V2.fusionIq.service.LessonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LessonServiceTest {

    @Mock
    private LessonRepo lessonRepo;

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private ActivityRepo activityRepo;

    @Mock
    private LessonModuleRepo lessonModuleRepo;

    @InjectMocks
    private LessonService lessonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveLesson_Success() {
        Long courseId = 1L;
        Lesson lesson = new Lesson();
        Course course = new Course();

        when(courseRepo.findById(courseId)).thenReturn(Optional.of(course));
        when(lessonRepo.save(lesson)).thenReturn(lesson);

        Lesson result = lessonService.saveLesson(lesson, courseId);

        assertEquals(lesson, result);
        verify(courseRepo, times(1)).findById(courseId);
        verify(lessonRepo, times(1)).save(lesson);
    }

    @Test
    void testFindLessonById_Success() {
        Long lessonId = 1L;
        Lesson lesson = new Lesson();

        when(lessonRepo.findById(lessonId)).thenReturn(Optional.of(lesson));

        Optional<Lesson> result = lessonService.findLessonById(lessonId);

        assertEquals(Optional.of(lesson), result);
        verify(lessonRepo, times(1)).findById(lessonId);
    }

    @Test
    void testFindAllLessons_Success() {
        List<Lesson> lessons = Arrays.asList(new Lesson(), new Lesson());

        when(lessonRepo.findAll()).thenReturn(lessons);

        List<Lesson> result = lessonService.findAllLessons();

        assertEquals(lessons, result);
        verify(lessonRepo, times(1)).findAll();
    }

    @Test
    void testDeleteLesson_Success() {
        Long lessonId = 1L;

        doNothing().when(activityRepo).deleteByLessonId(lessonId);
        doNothing().when(lessonRepo).deleteById(lessonId);

        lessonService.deleteLesson(lessonId);

        verify(activityRepo, times(1)).deleteByLessonId(lessonId);
        verify(lessonRepo, times(1)).deleteById(lessonId);
    }

    @Test
    void testFindLessonsByCourse_Success() {
        Long courseId = 1L;
        List<Lesson> lessons = Arrays.asList(new Lesson(), new Lesson());

        when(lessonRepo.findByCourseId(courseId)).thenReturn(lessons);

        List<Lesson> result = lessonService.findLessonsByCourse(courseId);

        assertEquals(lessons, result);
        verify(lessonRepo, times(1)).findByCourseId(courseId);
    }

    @Test
    void testGetLessonActivities_Success() {
        Long lessonId = 1L;
        List<Activity> activities = Arrays.asList(new Activity(), new Activity());

        when(activityRepo.findByLessonId(lessonId)).thenReturn(activities);

        List<Activity> result = lessonService.getLessonActivities(lessonId);

        assertEquals(activities, result);
        verify(activityRepo, times(1)).findByLessonId(lessonId);
    }

    @Test
    void testSavedLesson_Success() {
        Long lessonModuleId = 1L;
        Lesson lesson = new Lesson();
        LessonModule lessonModule = new LessonModule();

        when(lessonModuleRepo.findById(lessonModuleId)).thenReturn(Optional.of(lessonModule));
        when(lessonRepo.save(lesson)).thenReturn(lesson);

        Lesson result = lessonService.savedLesson(lesson, lessonModuleId);

        assertEquals(lesson, result);
        verify(lessonModuleRepo, times(1)).findById(lessonModuleId);
        verify(lessonRepo, times(1)).save(lesson);
    }

    @Test
    void testUpdateLesson_Success() {
        Long lessonModuleId = 1L;
        Lesson existingLesson = new Lesson();
        existingLesson.setLessonTitle("Old Title");

        Lesson updatedLesson = new Lesson();
        updatedLesson.setLessonTitle("New Title");

        when(lessonRepo.findByLessonModuleId(lessonModuleId)).thenReturn(Arrays.asList(existingLesson));
        when(lessonRepo.save(existingLesson)).thenReturn(existingLesson);

        Lesson result = lessonService.updateLesson(lessonModuleId, updatedLesson);

        assertEquals(existingLesson, result);
        assertEquals("New Title", result.getLessonTitle());
        verify(lessonRepo, times(1)).findByLessonModuleId(lessonModuleId);
        verify(lessonRepo, times(1)).save(existingLesson);
    }

    @Test
    void testGetLessonsByLessonModuleId_Success() {
        Long lessonModuleId = 1L;
        List<Lesson> lessons = Arrays.asList(new Lesson(), new Lesson());

        when(lessonRepo.findByLessonModuleId(lessonModuleId)).thenReturn(lessons);

        List<Lesson> result = lessonService.getLessonsByLessonModuleId(lessonModuleId);

        assertEquals(lessons, result);
        verify(lessonRepo, times(1)).findByLessonModuleId(lessonModuleId);
    }
}

