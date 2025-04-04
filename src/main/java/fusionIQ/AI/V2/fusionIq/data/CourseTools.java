package fusionIQ.AI.V2.fusionIq.data;


import jakarta.persistence.*;

import java.util.Arrays;

@Entity
public class CourseTools {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(length = 100000)
    private byte[] toolImage;
    private String toolName;
    private String skillName;
    @Column(length = 100000)
    private byte[] skillImage;


    private String coursePrerequisites;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getToolImage() {
        return toolImage;
    }

    public void setToolImage(byte[] toolImage) {
        this.toolImage = toolImage;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public byte[] getSkillImage() {
        return skillImage;
    }

    public void setSkillImage(byte[] skillImage) {
        this.skillImage = skillImage;
    }

    public String getCoursePrerequisites() {
        return coursePrerequisites;
    }

    public void setCoursePrerequisites(String coursePrerequisites) {
        this.coursePrerequisites = coursePrerequisites;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public CourseTools(Long id, byte[] toolImage, String toolName, String skillName, byte[] skillImage, String coursePrerequisites, Course course) {
        this.id = id;
        this.toolImage = toolImage;
        this.toolName = toolName;
        this.skillName = skillName;
        this.skillImage = skillImage;
        this.coursePrerequisites = coursePrerequisites;
        this.course = course;
    }

    @Override
    public String toString() {
        return "CourseTools{" +
                "id=" + id +
                ", toolImage=" + Arrays.toString(toolImage) +
                ", toolName='" + toolName + '\'' +
                ", skillName='" + skillName + '\'' +
                ", skillImage=" + Arrays.toString(skillImage) +
                ", coursePrerequisites='" + coursePrerequisites + '\'' +
                ", course=" + course +
                '}';
    }

    public CourseTools() {
    }
}


