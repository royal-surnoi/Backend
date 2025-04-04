//package fusionIQ.AI.V2.fusionIq.testcontroller;
//
//import fusionIQ.AI.V2.fusionIq.controller.TeacherFeedbackController;
//import fusionIQ.AI.V2.fusionIq.data.*;
//import fusionIQ.AI.V2.fusionIq.service.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(TeacherFeedbackController.class)
//class TeacherFeedbackControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private TeacherFeedbackService teacherFeedbackService;
//
//    @MockBean
//    private UserService userService;
//
//    @MockBean
//    private CourseService courseService;
//
//    @MockBean
//    private QuizService quizService;
//
//    @MockBean
//    private AssignmentService assignmentService;
//
//    @MockBean
//    private ProjectService projectService;
//
//    @MockBean
//    private LessonModuleService lessonModuleService;
//
//    @MockBean
//    private LessonService lessonService;
//
//    private TeacherFeedback feedback;
//    private User teacher;
//    private User student;
//    private Course course;
//    private Quiz quiz;
//    private Assignment assignment;
//    private Project project;
//    private LessonModule lessonModule;
//    private Lesson lesson;
//
//    @BeforeEach
//    void setUp() {
//        feedback = new TeacherFeedback();
//        feedback.setId(1L);
//        feedback.setFeedback("Good job");
//        feedback.setGrade("A");
//
//        teacher = new User();
//        teacher.setId(1L);
//        feedback.setTeacher(teacher);
//
//        student = new User();
//        student.setId(2L);
//        feedback.setStudent(student);
//
//        course = new Course();
//        course.setId(3L);
//        feedback.setCourse(course);
//
//        quiz = new Quiz();
//        quiz.setId(4L);
//        feedback.setQuiz(quiz);
//
//        assignment = new Assignment();
//        assignment.setId(5L);
//        feedback.setAssignment(assignment);
//
//        project = new Project();
//        project.setId(6L);
//        feedback.setProject(project);
//
//        lessonModule = new LessonModule();
//        lessonModule.setId(7L);
//        feedback.setLessonModule(lessonModule);
//
//        lesson = new Lesson();
//        lesson.setId(8L);
//        feedback.setLesson(lesson);
//    }
//
//
//    // Test case for getAllFeedback
//    @Test
//    void testGetAllFeedback() throws Exception {
//
//        MockitoAnnotations.openMocks(this);
//        TeacherFeedback feedback1 = new TeacherFeedback();
//        feedback1.setId(1L);
//        feedback1.setFeedback("Good job");
//        feedback1.setGrade("A");
//
//        TeacherFeedback feedback2 = new TeacherFeedback();
//        feedback2.setId(2L);
//        feedback2.setFeedback("Needs improvement");
//        feedback2.setGrade("C");
//
//        when(teacherFeedbackService.getAllFeedback()).thenReturn(Arrays.asList(feedback1, feedback2));
//
//        mockMvc.perform(get("/teacherFeedback/getAll"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].feedback").value("Good job"))
//                .andExpect(jsonPath("$[0].grade").value("A"))
//                .andExpect(jsonPath("$[1].feedback").value("Needs improvement"))
//                .andExpect(jsonPath("$[1].grade").value("C"));
//    }
//
//    // Test case for getFeedbackById
//    @Test
//    void testGetFeedbackById() throws Exception {
//        when(teacherFeedbackService.getFeedbackById(1L)).thenReturn(Optional.of(feedback));
//
//        mockMvc.perform(get("/teacherFeedback/get/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.feedback").value("Good job"))
//                .andExpect(jsonPath("$.grade").value("A"));
//    }
//
//    @Test
//    void testGetFeedbackById_NotFound() throws Exception {
//        when(teacherFeedbackService.getFeedbackById(1L)).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/teacherFeedback/get/1"))
//                .andExpect(status().isNotFound());
//    }
//
//    // Test case for updateFeedback with all fields
//    @Test
//    void testUpdateFeedback_WithAllFields() throws Exception {
//        TeacherFeedback updatedFeedback = new TeacherFeedback();
//        updatedFeedback.setId(1L);
//        updatedFeedback.setFeedback("Excellent work");
//        updatedFeedback.setGrade("A+");
//
//        when(teacherFeedbackService.getFeedbackById(1L)).thenReturn(Optional.of(feedback));
//        when(userService.findUserById(1L)).thenReturn(Optional.of(teacher));
//        when(userService.findUserById(2L)).thenReturn(Optional.of(student));
//        when(courseService.getCourseById(3L)).thenReturn(Optional.of(course));
//        when(quizService.getQuizsById(4L)).thenReturn(Optional.of(quiz));
//        when(assignmentService.getAssignmentById(5L)).thenReturn(Optional.of(assignment));
//        when(projectService.getProjectById(6L)).thenReturn(Optional.of(project));
//        when(lessonService.getLessonById(8L)).thenReturn(Optional.of(lesson));
//        when(lessonModuleService.getLessonModuleById(7L)).thenReturn(Optional.of(lessonModule));
//        when(teacherFeedbackService.updateFeedback(anyLong(), any(TeacherFeedback.class))).thenReturn(updatedFeedback);
//
//        mockMvc.perform(put("/teacherFeedback/update/1")
//                        .param("teacherId", "1")
//                        .param("studentId", "2")
//                        .param("courseId", "3")
//                        .param("quizId", "4")
//                        .param("assignmentId", "5")
//                        .param("projectId", "6")
//                        .param("lessonModuleId", "7")
//                        .param("lessonId", "8")
//                        .param("feedback", "Excellent work")
//                        .param("grade", "A+"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.feedback").value("Excellent work"))
//                .andExpect(jsonPath("$.grade").value("A+"));
//    }
//
//    @Test
//    void testUpdateFeedback_PartialFields() throws Exception {
//        TeacherFeedback updatedFeedback = new TeacherFeedback();
//        updatedFeedback.setId(1L);
//        updatedFeedback.setFeedback("Updated feedback");
//        updatedFeedback.setGrade("B");
//
//        when(teacherFeedbackService.getFeedbackById(1L)).thenReturn(Optional.of(feedback));
//        when(teacherFeedbackService.updateFeedback(anyLong(), any(TeacherFeedback.class))).thenReturn(updatedFeedback);
//
//        mockMvc.perform(put("/teacherFeedback/update/1")
//                        .param("feedback", "Updated feedback")
//                        .param("grade", "B"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.feedback").value("Updated feedback"))
//                .andExpect(jsonPath("$.grade").value("B"));
//    }
//
//    @Test
//    void testUpdateFeedback_NotFound() throws Exception {
//        when(teacherFeedbackService.getFeedbackById(1L)).thenReturn(Optional.empty());
//
//        mockMvc.perform(put("/teacherFeedback/update/1")
//                        .param("feedback", "Updated feedback")
//                        .param("grade", "B"))
//                .andExpect(status().isNotFound());
//    }
//
//    // Test case for deleteFeedback
//    @Test
//    void testDeleteFeedback() throws Exception {
//        doNothing().when(teacherFeedbackService).deleteFeedback(1L);
//
//        mockMvc.perform(delete("/teacherFeedback/delete/1"))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void testDeleteFeedback_NotFound() throws Exception {
//        doThrow(new IllegalArgumentException("Feedback not found")).when(teacherFeedbackService).deleteFeedback(1L);
//
//        mockMvc.perform(delete("/teacherFeedback/delete/1"))
//                .andExpect(status().isNotFound());
//    }
//
//    // Test case for getGradesByUserId
//    @Test
//    void testGetGradesByUserId() throws Exception {
//        when(teacherFeedbackService.getGradesByUserId(1L)).thenReturn(Arrays.asList("A", "B"));
//
//        mockMvc.perform(get("/teacherFeedback/gradesByUser/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0]").value("A"))
//                .andExpect(jsonPath("$[1]").value("B"));
//    }
//
//    // Test case for getFeedbackByStudentId
//    @Test
//    void testGetFeedbackByStudentId() throws Exception {
//        when(teacherFeedbackService.getFeedbackByStudentId(1L)).thenReturn(Arrays.asList(feedback));
//
//        mockMvc.perform(get("/teacherFeedback/student/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].feedback").value("Good job"))
//                .andExpect(jsonPath("$[0].grade").value("A"));
//    }
//
//    @Test
//    void testGetFeedbackByStudentId_NotFound() throws Exception {
//        when(teacherFeedbackService.getFeedbackByStudentId(1L)).thenReturn(Arrays.asList());
//
//        mockMvc.perform(get("/teacherFeedback/student/1"))
//                .andExpect(status().isNoContent());
//    }
//
//    // Test case for createFeedback with all fields
//    @Test
//    void testCreateFeedback_WithAllFields() throws Exception {
//        when(teacherFeedbackService.createFeedback(any(TeacherFeedback.class))).thenReturn(feedback);
//        when(userService.getUserById(1L)).thenReturn(teacher);
//        when(userService.getUserById(2L)).thenReturn(student);
//        when(courseService.getCourseById(3L)).thenReturn(Optional.of(course));
//        when(quizService.getQuizsById(4L)).thenReturn(Optional.of(quiz));
//        when(assignmentService.getAssignmentById(5L)).thenReturn(Optional.of(assignment));
//        when(projectService.getProjectById(6L)).thenReturn(Optional.of(project));
//        when(lessonModuleService.getLessonModuleById(7L)).thenReturn(Optional.of(lessonModule));
//        when(lessonService.getLessonById(8L)).thenReturn(Optional.of(lesson));
//
//        mockMvc.perform(post("/teacherFeedback/create")
//                        .param("teacherId", "1")
//                        .param("studentId", "2")
//                        .param("courseId", "3")
//                        .param("quizId", "4")
//                        .param("assignmentId", "5")
//                        .param("projectId", "6")
//                        .param("lessonModuleId", "7")
//                        .param("lessonId", "8")
//                        .param("feedback", "Good job")
//                        .param("grade", "A"))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.feedback").value("Good job"))
//                .andExpect(jsonPath("$.grade").value("A"));
//    }
//
//    // Test case for createFeedback with missing required fields (teacherId and studentId)
//    @Test
//    void testCreateFeedback_MissingRequiredFields() throws Exception {
//        mockMvc.perform(post("/teacherFeedback/create")
//                        .param("feedback", "Good job")
//                        .param("grade", "A"))
//                .andExpect(status().isBadRequest());
//    }
//
////    @Test
////    void testGetFeedback_WithAllFields() throws Exception {
////        // Mock the service to return the feedback object in a list
////        when(teacherFeedbackService.getFeedback(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L)).thenReturn(List.of(feedback));
////
////        // Perform the GET request and verify the response
////        mockMvc.perform(get("/teacherFeedback/feedback")
////                        .param("teacherId", "1")
////                        .param("studentId", "2")
////                        .param("courseId", "3")
////                        .param("quizId", "4")
////                        .param("assignmentId", "5")
////                        .param("projectId", "6")
////                        .param("lessonModuleId", "7")
////                        .param("lessonId", "8"))
////                .andDo(print()) // This will print the response details
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$[0].feedback").value("Great work!"))
////                .andExpect(jsonPath("$[0].grade").value("A"));
////
////    }
//
//    // Test case for getFeedback with some optional parameters missing
////    @Test
////    void testGetFeedback_WithMissingFields() throws Exception {
////        when(teacherFeedbackService.getFeedback(1L, null, 3L, null, null, null, null, null)).thenReturn(List.of(feedback));
////
////        mockMvc.perform(get("/teacherFeedback/feedback")
////                        .param("teacherId", "1")
////                        .param("courseId", "3"))
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$[0].feedback").value("Good job"))
////                .andExpect(jsonPath("$[0].grade").value("A"));
////    }
//}
