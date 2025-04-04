package fusionIQ.AI.V2.fusionIq.testservice;



import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.*;
import fusionIQ.AI.V2.fusionIq.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private LessonRepo lessonRepo;

    @Mock
    private EnrollmentRepo enrollmentRepo;

    @Mock
    private ProjectRepo projectRepo;

    @Mock
    private ReviewRepo reviewRepo;

    @Mock
    private VideoRepo videoRepo;

    @Mock
    private RatingRepo ratingRepo;

    @Mock
    private SubmissionRepo submissionRepo;

    @Mock
    private AssignmentRepo assignmentRepo;

    @Mock
    private CourseToolRepo courseToolRepo;

    @Mock
    private SubmitProjectRepo submitProjectRepo;

    @Mock
    private AnnouncementRepo announcementRepo;

    @Mock
    private LessonModuleRepo lessonModuleRepo;

    @Mock
    private QuizRepo quizRepo;

    @Mock
    private VideoProgressRepo videoProgressRepo;

    @Mock
    private AIAssignmentRepo aiAssignmentRepo;

    @Mock
    private AIQuizRepo aiQuizRepo;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCourse() {
        User user = new User();
        user.setId(1L);

        Course course = new Course();
        course.setCourseTitle("Test Course");

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(courseRepo.save(any(Course.class))).thenReturn(course);

        Course savedCourse = courseService.saveCourse(course, 1L);

        assertNotNull(savedCourse);
        assertEquals("Test Course", savedCourse.getCourseTitle());
        verify(courseRepo, times(1)).save(course);
    }

    @Test
    void testFindCourseById() {
        Course course = new Course();
        course.setId(1L);

        when(courseRepo.findById(anyLong())).thenReturn(Optional.of(course));

        Optional<Course> foundCourse = courseService.findCourseById(1L);

        assertTrue(foundCourse.isPresent());
        assertEquals(1L, foundCourse.get().getId());
        verify(courseRepo, times(1)).findById(1L);
    }

    @Test
    void testFindAllCourses() {
        List<Course> courses = Arrays.asList(new Course(), new Course());

        when(courseRepo.findAll()).thenReturn(courses);

        List<Course> foundCourses = courseService.findAllCourses();

        assertEquals(2, foundCourses.size());
        verify(courseRepo, times(1)).findAll();
    }

    @Test
    void testDeleteCourseById() {
        doNothing().when(videoProgressRepo).deleteByCourseId(anyLong());
        doNothing().when(lessonRepo).deleteByCourseId(anyLong());
        doNothing().when(enrollmentRepo).deleteByCourseId(anyLong());
        doNothing().when(projectRepo).deleteByCourseId(anyLong());
        doNothing().when(reviewRepo).deleteByCourseId(anyLong());
        doNothing().when(videoRepo).deleteByCourseId(anyLong());
        doNothing().when(ratingRepo).deleteByCourseId(anyLong());
        doNothing().when(submissionRepo).deleteByCourseId(anyLong());
        doNothing().when(assignmentRepo).deleteByCourseId(anyLong());
        doNothing().when(courseToolRepo).deleteByCourseId(anyLong());
        doNothing().when(submitProjectRepo).deleteByCourseId(anyLong());
        doNothing().when(announcementRepo).deleteByCourseId(anyLong());
        doNothing().when(lessonModuleRepo).deleteByCourseId(anyLong());
        doNothing().when(aiQuizRepo).deleteByCourseId(anyLong());
        doNothing().when(aiAssignmentRepo).deleteByCourseId(anyLong());
        doNothing().when(courseRepo).deleteById(anyLong());

        courseService.deleteCourseById(1L);

        verify(courseRepo, times(1)).deleteById(1L);
    }

    @Test
    void testGetCourseLessons() {
        List<Lesson> lessons = Arrays.asList(new Lesson(), new Lesson());

        when(lessonRepo.findByCourseId(anyLong())).thenReturn(lessons);

        List<Lesson> foundLessons = courseService.getCourseLessons(1L);

        assertEquals(2, foundLessons.size());
        verify(lessonRepo, times(1)).findByCourseId(1L);
    }

    @Test
    void testGetCourseEnrollments() {
        List<Enrollment> enrollments = Arrays.asList(new Enrollment(), new Enrollment());

        when(enrollmentRepo.findByCourseId(anyLong())).thenReturn(enrollments);

        List<Enrollment> foundEnrollments = courseService.getCourseEnrollments(1L);

        assertEquals(2, foundEnrollments.size());
        verify(enrollmentRepo, times(1)).findByCourseId(1L);
    }

    @Test
    void testGeneratePromoCode() {
        Course course = new Course();
        course.setId(1L);
        course.setCourseFee(1000L);

        when(courseRepo.findById(anyLong())).thenReturn(Optional.of(course));
        when(courseRepo.save(any(Course.class))).thenReturn(course);

        Course updatedCourse = courseService.generatePromoCode(1L, 20L, LocalDateTime.now().plusDays(7), 1000L, "USD", "50%");

        assertNotNull(updatedCourse.getPromoCode());
        assertEquals(20L, updatedCourse.getDiscountPercentage());
        verify(courseRepo, times(1)).save(course);
    }

    @Test
    void testApplyPromoCode() {
        Course course = new Course();
        course.setPromoCode("PROMO123");
        course.setPromoCodeExpiration(LocalDateTime.now().plusDays(1));
        course.setDiscountPercentage(20L);
        course.setCourseFee(1000L);

        when(courseRepo.findById(anyLong())).thenReturn(Optional.of(course));
        when(courseRepo.save(any(Course.class))).thenReturn(course);

        Course updatedCourse = courseService.applyPromoCode(1L, "PROMO123");

        assertEquals(800L, updatedCourse.getDiscountFee());
        verify(courseRepo, times(1)).save(course);
    }

    @Test
    void testAddCourse() throws IOException {
        User user = new User();
        user.setId(1L);
        MultipartFile courseImage = mock(MultipartFile.class);
        MultipartFile courseDocument = mock(MultipartFile.class);

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(courseRepo.save(any(Course.class))).thenReturn(new Course());

        Course savedCourse = courseService.addCourse(
                1L,
                "Course Title",
                "Course Description",
                "EN",
                Course.Level.Beginner,
                "L1",
                "L2",
                "L3",
                "L4",
                "L5",
                "L6",
                "L7",
                "L8",
                courseImage,
                "PROMO123",
                1000L,
                "USD",
                200L,
                20L,
                "10 hours",
                "Online",
                "50%",
                courseDocument
        );

        assertNotNull(savedCourse);
        verify(courseRepo, times(1)).save(any(Course.class));
    }

    @Test
    void testUpdateCourseFields() throws IOException {
        Course existingCourse = new Course();
        existingCourse.setCourseTitle("Old Title");
        Course courseUpdates = new Course();
        courseUpdates.setCourseTitle("New Title");

        MultipartFile courseImage = mock(MultipartFile.class);
        when(courseImage.isEmpty()).thenReturn(false);
        when(courseImage.getBytes()).thenReturn("imageBytes".getBytes());

        courseService.updateCourseFields(existingCourse, courseUpdates, courseImage);

        assertEquals("New Title", existingCourse.getCourseTitle());
        verify(courseRepo, never()).save(existingCourse);
    }

    @Test
    void testAddNewCourse() throws IOException {
        User user = new User();
        user.setId(1L);
        MultipartFile courseImage = mock(MultipartFile.class);

        when(userRepo.findById(anyLong())).thenReturn(Optional.of(user));
        when(courseRepo.save(any(Course.class))).thenReturn(new Course());

        Course savedCourse = courseService.addNewCourse(
                1L,
                "Course Title",
                "Course Description",
                "EN",
                Course.Level.Beginner,
                "L1",
                "L2",
                "L3",
                "L4",
                "L5",
                "L6",
                "L7",
                "L8",
                courseImage,
                "10 hours",
                "Online",
                "Full Term"
        );

        assertNotNull(savedCourse);
        verify(courseRepo, times(1)).save(any(Course.class));
    }

    @Test
    void testPatchCourseById() {
        Course existingCourse = new Course();
        existingCourse.setId(1L);
        existingCourse.setCourseTitle("Old Title");

        Map<String, Object> updates = new HashMap<>();
        updates.put("courseTitle", "New Title");

        when(courseRepo.findById(anyLong())).thenReturn(Optional.of(existingCourse));
        when(courseRepo.save(any(Course.class))).thenReturn(existingCourse);

        Course updatedCourse = courseService.patchCourseById(1L, updates);

        assertEquals("New Title", updatedCourse.getCourseTitle());
        verify(courseRepo, times(1)).save(existingCourse);
    }
}

