package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class TrainingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    private User teacher;
    @ManyToMany
    private Set<User> students = new HashSet<>();
    private String conferenceUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime scheduledTime;

    @ManyToOne
    @JoinColumn
    private Course course;

    @ManyToOne
    @JoinColumn(name = "mock_id")
    private MockTestInterview mockTestInterview;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public Set<User> getStudents() {
        return students;
    }

    public void setStudents(Set<User> students) {
        this.students = students;
    }

    public String getConferenceUrl() {
        return conferenceUrl;
    }

    public void setConferenceUrl(String conferenceUrl) {
        this.conferenceUrl = conferenceUrl;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public MockTestInterview getMockTestInterview() {
        return mockTestInterview;
    }

    public void setMockTestInterview(MockTestInterview mockTestInterview) {
        this.mockTestInterview = mockTestInterview;
    }

    public TrainingRoom(Long id, String name, User teacher, Set<User> students, String conferenceUrl, LocalDateTime scheduledTime, Course course, MockTestInterview mockTestInterview) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.students = students;
        this.conferenceUrl = conferenceUrl;
        this.scheduledTime = scheduledTime;
        this.course = course;
        this.mockTestInterview = mockTestInterview;
    }

    public TrainingRoom() {
        this.scheduledTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "TrainingRoom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teacher=" + teacher +
                ", students=" + students +
                ", conferenceUrl='" + conferenceUrl + '\'' +
                ", scheduledTime=" + scheduledTime +
                ", course=" + course +
                ", mockTestInterview=" + mockTestInterview +
                '}';
    }
}
