package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

@Entity
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private String userAnnouncementTitle;
    private String userAnnouncementDescription;
    private String courseAnnouncementTitle;
    private String courseAnnouncementDescription;

    public Announcement() {
    }

    public Announcement(long id, Course course, String userAnnouncementTitle, String userAnnouncementDescription, String courseAnnouncementTitle, String courseAnnouncementDescription) {
        this.id = id;
        this.course = course;
        this.userAnnouncementTitle = userAnnouncementTitle;
        this.userAnnouncementDescription = userAnnouncementDescription;
        this.courseAnnouncementTitle = courseAnnouncementTitle;
        this.courseAnnouncementDescription = courseAnnouncementDescription;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getUserAnnouncementTitle() {
        return userAnnouncementTitle;
    }

    public void setUserAnnouncementTitle(String userAnnouncementTitle) {
        this.userAnnouncementTitle = userAnnouncementTitle;
    }

    public String getUserAnnouncementDescription() {
        return userAnnouncementDescription;
    }

    public void setUserAnnouncementDescription(String userAnnouncementDescription) {
        this.userAnnouncementDescription = userAnnouncementDescription;
    }

    public String getCourseAnnouncementTitle() {
        return courseAnnouncementTitle;
    }

    public void setCourseAnnouncementTitle(String courseAnnouncementTitle) {
        this.courseAnnouncementTitle = courseAnnouncementTitle;
    }

    public String getCourseAnnouncementDescription() {
        return courseAnnouncementDescription;
    }

    public void setCourseAnnouncementDescription(String courseAnnouncementDescription) {
        this.courseAnnouncementDescription = courseAnnouncementDescription;
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "id=" + id +
                ", course=" + course +
                ", userAnnouncementTitle='" + userAnnouncementTitle + '\'' +
                ", userAnnouncementDescription='" + userAnnouncementDescription + '\'' +
                ", courseAnnouncementTitle='" + courseAnnouncementTitle + '\'' +
                ", courseAnnouncementDescription='" + courseAnnouncementDescription + '\'' +
                '}';
    }
}
