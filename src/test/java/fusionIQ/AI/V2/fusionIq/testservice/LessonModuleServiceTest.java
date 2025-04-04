package fusionIQ.AI.V2.fusionIq.testservice;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.LessonModule;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.LessonModuleRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.LessonModuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class LessonModuleServiceTest {

    @Mock
    private LessonModuleRepo lessonModuleRepo;

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private LessonModuleService lessonModuleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveLessonModule_Success() {
        Long courseId = 1L;
        LessonModule lessonModule = new LessonModule();
        Course course = new Course();

        when(courseRepo.findById(courseId)).thenReturn(Optional.of(course));
        when(lessonModuleRepo.save(lessonModule)).thenReturn(lessonModule);

        LessonModule result = lessonModuleService.saveLessonModule(lessonModule, courseId);

        assertEquals(lessonModule, result);
        verify(courseRepo, times(1)).findById(courseId);
        verify(lessonModuleRepo, times(1)).save(lessonModule);
    }

    @Test
    void testSaveLessonModule_CourseNotFound() {
        Long courseId = 1L;
        LessonModule lessonModule = new LessonModule();

        when(courseRepo.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> lessonModuleService.saveLessonModule(lessonModule, courseId));

        verify(courseRepo, times(1)).findById(courseId);
        verify(lessonModuleRepo, never()).save(lessonModule);
    }

    @Test
    void testGetAllLessonModules_Success() {
        List<LessonModule> lessonModules = Arrays.asList(new LessonModule(), new LessonModule());

        when(lessonModuleRepo.findAll()).thenReturn(lessonModules);

        List<LessonModule> result = lessonModuleService.getAllLessonModules();

        assertEquals(lessonModules, result);
        verify(lessonModuleRepo, times(1)).findAll();
    }



    @Test
    void testDeleteLessonModule_Success() {
        Long lessonModuleId = 1L;
        LessonModule lessonModule = new LessonModule();

        when(lessonModuleRepo.findById(lessonModuleId)).thenReturn(Optional.of(lessonModule));
        doNothing().when(lessonModuleRepo).delete(lessonModule);

        lessonModuleService.deleteLessonModule(lessonModuleId);

        verify(lessonModuleRepo, times(1)).findById(lessonModuleId);
        verify(lessonModuleRepo, times(1)).delete(lessonModule);
    }

    @Test
    void testDeleteLessonModule_NotFound() {
        Long lessonModuleId = 1L;

        when(lessonModuleRepo.findById(lessonModuleId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> lessonModuleService.deleteLessonModule(lessonModuleId));

        verify(lessonModuleRepo, times(1)).findById(lessonModuleId);
        verify(lessonModuleRepo, never()).delete(any());
    }

    @Test
    void testUpdateLessonModuleByIdAndCourseId_Success() {
        Long lessonModuleId = 1L;
        Long courseId = 1L;
        LessonModule existingLessonModule = new LessonModule();
        LessonModule updatedLessonModule = new LessonModule();
        updatedLessonModule.setModuleName("Updated Module");

        when(lessonModuleRepo.findByIdAndCourseId(lessonModuleId, courseId)).thenReturn(Optional.of(existingLessonModule));
        when(lessonModuleRepo.save(existingLessonModule)).thenReturn(existingLessonModule);

        LessonModule result = lessonModuleService.updateLessonModuleByIdAndCourseId(lessonModuleId, courseId, updatedLessonModule);

        assertEquals(existingLessonModule, result);
        assertEquals("Updated Module", result.getModuleName());
        verify(lessonModuleRepo, times(1)).findByIdAndCourseId(lessonModuleId, courseId);
        verify(lessonModuleRepo, times(1)).save(existingLessonModule);
    }

    @Test
    void testUpdateLessonModuleByIdAndCourseId_NotFound() {
        Long lessonModuleId = 1L;
        Long courseId = 1L;
        LessonModule updatedLessonModule = new LessonModule();

        when(lessonModuleRepo.findByIdAndCourseId(lessonModuleId, courseId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> lessonModuleService.updateLessonModuleByIdAndCourseId(lessonModuleId, courseId, updatedLessonModule));

        verify(lessonModuleRepo, times(1)).findByIdAndCourseId(lessonModuleId, courseId);
        verify(lessonModuleRepo, never()).save(any());
    }

    @Test
    void testSaveLessonModuleByUser_Success() {
        Long userId = 1L;
        LessonModule lessonModule = new LessonModule();
        User user = new User();

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(lessonModuleRepo.save(lessonModule)).thenReturn(lessonModule);

        LessonModule result = lessonModuleService.saveLessonModuleByUser(lessonModule, userId);

        assertEquals(lessonModule, result);
        verify(userRepo, times(1)).findById(userId);
        verify(lessonModuleRepo, times(1)).save(lessonModule);
    }

    @Test
    void testSaveLessonModuleByUser_UserNotFound() {
        Long userId = 1L;
        LessonModule lessonModule = new LessonModule();

        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> lessonModuleService.saveLessonModuleByUser(lessonModule, userId));

        verify(userRepo, times(1)).findById(userId);
        verify(lessonModuleRepo, never()).save(lessonModule);
    }
}

