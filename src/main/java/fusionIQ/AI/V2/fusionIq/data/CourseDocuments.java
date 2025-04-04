package fusionIQ.AI.V2.fusionIq.data;


import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class CourseDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] courseDocument;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private Course course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getCourseDocument() {
        return courseDocument;
    }

    public void setCourseDocument(byte[] courseDocument) {
        this.courseDocument = courseDocument;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public CourseDocuments(Long id, byte[] courseDocument, Course course) {
        this.id = id;
        this.courseDocument = courseDocument;
        this.course = course;
    }

    public CourseDocuments() {
    }

    @Override
    public String toString() {
        return "CourseDocuments{" +
                "id=" + id +
                ", courseDocument=" + Arrays.toString(courseDocument) +
                ", course=" + course +
                '}';
    }
}
