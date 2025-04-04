
package fusionIQ.AI.V2.fusionIq.testdata;


import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.CourseTools;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CourseToolsTests {

    @Test
    void testCourseToolsConstructorAndGetters() {
        Course course = new Course();
        course.setId(1L); // Initialize the course ID or other necessary fields

        byte[] toolImage = new byte[]{1, 2, 3};
        byte[] skillImage = new byte[]{4, 5, 6};

        CourseTools courseTool = new CourseTools(1L, toolImage, "Test Tool", "Test Skill", skillImage, "None", course);

        // Assertions
        assertThat(courseTool.getId()).isEqualTo(1L);
        assertThat(courseTool.getToolImage()).isEqualTo(toolImage);
        assertThat(courseTool.getToolName()).isEqualTo("Test Tool");
        assertThat(courseTool.getSkillName()).isEqualTo("Test Skill");
        assertThat(courseTool.getSkillImage()).isEqualTo(skillImage);
        assertThat(courseTool.getCoursePrerequisites()).isEqualTo("None");
        assertThat(courseTool.getCourse()).isEqualTo(course);
    }

    @Test
    void testCourseToolsSetters() {
        Course course = new Course();
        course.setId(2L); // Initialize the course ID or other necessary fields

        CourseTools courseTool = new CourseTools();

        // Set properties using setters
        courseTool.setId(2L);
        courseTool.setToolImage(new byte[]{7, 8, 9});
        courseTool.setToolName("Another Tool");
        courseTool.setSkillName("Another Skill");
        courseTool.setSkillImage(new byte[]{10, 11, 12});
        courseTool.setCoursePrerequisites("Basic Knowledge");
        courseTool.setCourse(course);

        // Assertions
        assertThat(courseTool.getId()).isEqualTo(2L);
        assertThat(courseTool.getToolImage()).isEqualTo(new byte[]{7, 8, 9});
        assertThat(courseTool.getToolName()).isEqualTo("Another Tool");
        assertThat(courseTool.getSkillName()).isEqualTo("Another Skill");
        assertThat(courseTool.getSkillImage()).isEqualTo(new byte[]{10, 11, 12});
        assertThat(courseTool.getCoursePrerequisites()).isEqualTo("Basic Knowledge");
        assertThat(courseTool.getCourse()).isEqualTo(course);
    }

    @Test
    void testToString() {
        Course course = new Course();
        course.setId(3L); // Initialize the course ID or other necessary fields

        CourseTools courseTool = new CourseTools(3L, new byte[]{1}, "Tool Name", "Skill Name", new byte[]{2}, "Prerequisites", course);

        // Check if the toString method contains relevant details
        String expectedString = "CourseTools{id=3, toolImage=[1], toolName='Tool Name', skillName='Skill Name', skillImage=[2], coursePrerequisites='Prerequisites', course=" + course + "}";
        assertThat(courseTool.toString()).isEqualTo(expectedString);
    }
}

