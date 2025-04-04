package fusionIQ.AI.V2.fusionIq.testdata;

import fusionIQ.AI.V2.fusionIq.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectTests {

    private Project project;
    private Course course;
    private User user, teacher, student;
    private List<Submission> submissions;
    private List<Grade> grades;

    @BeforeEach
    public void setUp() {
        // Create dummy Course, User, Teacher, and Student
        course = new Course();
        course.setId(1L);

        user = new User();
        user.setId(1L);

        teacher = new User();
        teacher.setId(2L);

        student = new User();
        student.setId(3L);

        // Create dummy submissions
        submissions = new ArrayList<>();
        Submission submission1 = new Submission();
        submission1.setId(1L);
        submissions.add(submission1);

        // Create dummy grades
        grades = new ArrayList<>();
        Grade grade1 = new Grade();
        grade1.setId(1L);
        grades.add(grade1);

        // Initialize the Project object
        project = new Project();
        project.setId(1L);
        project.setCourse(course);
        project.setProjectTitle("AI Research Project");
        project.setProjectDescription("A project to research AI in education.");
        project.setUser(user);
        project.setTeacher(teacher);
        project.setStudent(student);
        project.setProjectDocument(new byte[]{1, 2, 3});
        project.setProjectDeadline(LocalDateTime.now().plusMonths(1));
        project.setStartDate(LocalDateTime.now());
        project.setReviewMeetDate(LocalDateTime.now().plusWeeks(2));
        project.setMaxTeam(4L);
        project.setGitUrl("https://github.com/project/repo");
        project.setStudentIds("1,2,3");
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        project.setSubmissions(submissions);
        project.setGrades(grades);
    }

    @Test
    public void testGetters() {
        assertEquals(1L, project.getId());
        assertEquals(course, project.getCourse());
        assertEquals("AI Research Project", project.getProjectTitle());
        assertEquals("A project to research AI in education.", project.getProjectDescription());
        assertEquals(user, project.getUser());
        assertEquals(teacher, project.getTeacher());
        assertEquals(student, project.getStudent());
        assertArrayEquals(new byte[]{1, 2, 3}, project.getProjectDocument());
        assertNotNull(project.getProjectDeadline());
        assertNotNull(project.getStartDate());
        assertNotNull(project.getReviewMeetDate());
        assertEquals(4L, project.getMaxTeam());
        assertEquals("https://github.com/project/repo", project.getGitUrl());
        assertEquals("1,2,3", project.getStudentIds());
        assertNotNull(project.getCreatedAt());
        assertNotNull(project.getUpdatedAt());
        assertEquals(submissions, project.getSubmissions());
        assertEquals(grades, project.getGrades());
    }

    @Test
    public void testSetters() {
        project.setProjectTitle("Updated Project Title");
        project.setProjectDescription("Updated description");
        project.setGitUrl("https://github.com/new/repo");
        project.setMaxTeam(6L);

        assertEquals("Updated Project Title", project.getProjectTitle());
        assertEquals("Updated description", project.getProjectDescription());
        assertEquals("https://github.com/new/repo", project.getGitUrl());
        assertEquals(6L, project.getMaxTeam());
    }

    @Test
    public void testEquality() {
        // Comparing each field manually
        Project expectedProject = new Project();
        expectedProject.setId(1L);
        expectedProject.setCourse(course);
        expectedProject.setProjectTitle("AI Research Project");
        expectedProject.setProjectDescription("A project to research AI in education.");
        expectedProject.setUser(user);
        expectedProject.setTeacher(teacher);
        expectedProject.setStudent(student);
        expectedProject.setProjectDocument(new byte[]{1, 2, 3});
        expectedProject.setProjectDeadline(project.getProjectDeadline());
        expectedProject.setStartDate(project.getStartDate());
        expectedProject.setReviewMeetDate(project.getReviewMeetDate());
        expectedProject.setMaxTeam(4L);
        expectedProject.setGitUrl("https://github.com/project/repo");
        expectedProject.setStudentIds("1,2,3");
        expectedProject.setCreatedAt(project.getCreatedAt());
        expectedProject.setUpdatedAt(project.getUpdatedAt());
        expectedProject.setSubmissions(submissions);
        expectedProject.setGrades(grades);

        // Manual field-by-field comparison
        assertEquals(expectedProject.getId(), project.getId());
        assertEquals(expectedProject.getCourse().getId(), project.getCourse().getId());
        assertEquals(expectedProject.getProjectTitle(), project.getProjectTitle());
        assertEquals(expectedProject.getProjectDescription(), project.getProjectDescription());
        assertEquals(expectedProject.getUser().getId(), project.getUser().getId());
        assertEquals(expectedProject.getTeacher().getId(), project.getTeacher().getId());
        assertEquals(expectedProject.getStudent().getId(), project.getStudent().getId());
        assertArrayEquals(expectedProject.getProjectDocument(), project.getProjectDocument());
        assertEquals(expectedProject.getProjectDeadline(), project.getProjectDeadline());
        assertEquals(expectedProject.getStartDate(), project.getStartDate());
        assertEquals(expectedProject.getReviewMeetDate(), project.getReviewMeetDate());
        assertEquals(expectedProject.getMaxTeam(), project.getMaxTeam());
        assertEquals(expectedProject.getGitUrl(), project.getGitUrl());
        assertEquals(expectedProject.getStudentIds(), project.getStudentIds());
        assertEquals(expectedProject.getCreatedAt(), project.getCreatedAt());
        assertEquals(expectedProject.getUpdatedAt(), project.getUpdatedAt());
        assertEquals(expectedProject.getSubmissions().size(), project.getSubmissions().size());
        assertEquals(expectedProject.getGrades().size(), project.getGrades().size());
    }

    @Test
    public void testDefaultConstructor() {
        Project defaultProject = new Project();
        assertNotNull(defaultProject.getCreatedAt());
        assertNotNull(defaultProject.getUpdatedAt());
        assertNotNull(defaultProject.getReviewMeetDate());
        assertNotNull(defaultProject.getStartDate());
        assertNotNull(defaultProject.getProjectDeadline());
    }

    @Test
    public void testManualStringComparison() {
        String actualString = project.toString();
        assertTrue(actualString.contains("Project{id=1"));
        assertTrue(actualString.contains("course=Course{id=1"));
        assertTrue(actualString.contains("projectTitle='AI Research Project'"));
        assertTrue(actualString.contains("projectDescription='A project to research AI in education.'"));
        assertTrue(actualString.contains("user=User{id=1"));
        assertTrue(actualString.contains("teacher=User{id=2"));
        assertTrue(actualString.contains("student=User{id=3"));
        assertTrue(actualString.contains("gitUrl='https://github.com/project/repo'"));
    }
}
