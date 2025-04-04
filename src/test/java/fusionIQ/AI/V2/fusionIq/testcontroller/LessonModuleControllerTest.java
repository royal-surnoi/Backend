package fusionIQ.AI.V2.fusionIq.testcontroller;

import fusionIQ.AI.V2.fusionIq.controller.LessonModuleController;
import fusionIQ.AI.V2.fusionIq.data.LessonModule;
import fusionIQ.AI.V2.fusionIq.service.LessonModuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LessonModuleControllerTest {

    @Mock
    private LessonModuleService lessonModuleService;

    @InjectMocks
    private LessonModuleController lessonModuleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateLessonModule_Success() {
        Long courseId = 1L;
        LessonModule lessonModule = new LessonModule();
        LessonModule savedLessonModule = new LessonModule();

        when(lessonModuleService.saveLessonModule(lessonModule, courseId)).thenReturn(savedLessonModule);

        ResponseEntity<LessonModule> response = lessonModuleController.createLesson(lessonModule, courseId);

        assertEquals(ResponseEntity.ok(savedLessonModule), response);
        verify(lessonModuleService, times(1)).saveLessonModule(lessonModule, courseId);
    }

    @Test
    void testGetAllLessonModules_Success() {
        List<LessonModule> lessonModules = Arrays.asList(new LessonModule(), new LessonModule());

        when(lessonModuleService.getAllLessonModules()).thenReturn(lessonModules);

        ResponseEntity<List<LessonModule>> response = lessonModuleController.getAllLessonModules();

        assertEquals(ResponseEntity.ok(lessonModules), response);
        verify(lessonModuleService, times(1)).getAllLessonModules();
    }

    @Test
    void testGetLessonModulesByCourseId_Success() {
        Long courseId = 1L;
        List<LessonModule> lessonModules = Arrays.asList(new LessonModule(), new LessonModule());

        when(lessonModuleService.getLessonModulesByCourseId(courseId)).thenReturn(lessonModules);

        ResponseEntity<List<LessonModule>> response = lessonModuleController.getLessonModulesByCourseId(courseId);

        assertEquals(ResponseEntity.ok(lessonModules), response);
        verify(lessonModuleService, times(1)).getLessonModulesByCourseId(courseId);
    }

    @Test
    void testGetLessonModulesByCourseId_NotFound() {
        Long courseId = 1L;

        when(lessonModuleService.getLessonModulesByCourseId(courseId)).thenReturn(Arrays.asList());

        ResponseEntity<List<LessonModule>> response = lessonModuleController.getLessonModulesByCourseId(courseId);

        assertEquals(ResponseEntity.notFound().build(), response);
        verify(lessonModuleService, times(1)).getLessonModulesByCourseId(courseId);
    }

    @Test
    void testDeleteLessonModule_Success() {
        Long lessonModuleId = 1L;

        doNothing().when(lessonModuleService).deleteLessonModule(lessonModuleId);

        ResponseEntity<?> response = lessonModuleController.deleteLessonModule(lessonModuleId);

        assertEquals(ResponseEntity.ok().build(), response);
        verify(lessonModuleService, times(1)).deleteLessonModule(lessonModuleId);
    }

    @Test
    void testUpdateLessonModuleByIdAndCourseId_Success() {
        Long courseId = 1L;
        Long lessonModuleId = 1L;
        LessonModule updatedLessonModule = new LessonModule();

        when(lessonModuleService.updateLessonModuleByIdAndCourseId(lessonModuleId, courseId, updatedLessonModule))
                .thenReturn(updatedLessonModule);

        ResponseEntity<LessonModule> response = lessonModuleController.updateLessonModuleByIdAndCourseId(courseId, lessonModuleId, updatedLessonModule);

        assertEquals(ResponseEntity.ok(updatedLessonModule), response);
        verify(lessonModuleService, times(1)).updateLessonModuleByIdAndCourseId(lessonModuleId, courseId, updatedLessonModule);
    }

    @Test
    void testCreateLessonByUser_Success() {
        Long userId = 1L;
        LessonModule lessonModule = new LessonModule();
        LessonModule savedLessonModule = new LessonModule();

        when(lessonModuleService.saveLessonModuleByUser(lessonModule, userId)).thenReturn(savedLessonModule);

        ResponseEntity<LessonModule> response = lessonModuleController.createLessonByUser(lessonModule, userId);

        assertEquals(ResponseEntity.ok(savedLessonModule), response);
        verify(lessonModuleService, times(1)).saveLessonModuleByUser(lessonModule, userId);
    }
}
