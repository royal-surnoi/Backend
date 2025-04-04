package fusionIQ.AI.V2.fusionIq.testcontroller;



import fusionIQ.AI.V2.fusionIq.controller.CourseController;
import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveCourse() {
        Course course = new Course();
        course.setCourseTitle("Test Course");

        when(courseService.saveCourse(course, 1L)).thenReturn(course);

        ResponseEntity<Course> response = courseController.save(course, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(course, response.getBody());
    }

    @Test
    public void testGetCourseById() {
        Course course = new Course();
        course.setId(1L);
        course.setCourseTitle("Test Course");

        when(courseService.findCourseById(1L)).thenReturn(Optional.of(course));

        ResponseEntity<Course> response = courseController.getCourseById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(course, response.getBody());
    }

    @Test
    public void testGetAllCourses() {
        Course course1 = new Course();
        course1.setId(1L);
        course1.setCourseTitle("Test Course 1");

        Course course2 = new Course();
        course2.setId(2L);
        course2.setCourseTitle("Test Course 2");

        List<Course> courseList = Arrays.asList(course1, course2);

        when(courseService.findAllCourses()).thenReturn(courseList);

        ResponseEntity<List<Course>> response = courseController.getAllCourses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(courseList, response.getBody());
    }

    @Test
    public void testUpdateCourse() {
        Course course = new Course();
        course.setId(1L);
        course.setCourseTitle("Updated Course");

        MultipartFile toolImage = mock(MultipartFile.class);

        when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
        when(courseService.savingCourse(course)).thenReturn(course);

        ResponseEntity<Course> response = courseController.updateCourse(1L, course, toolImage);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(course, response.getBody());
    }

    @Test
    public void testDeleteCourse() {
        doNothing().when(courseService).deleteCourseById(1L);

        ResponseEntity<?> response = courseController.deleteCourseById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(courseService, times(1)).deleteCourseById(1L);
    }
}
