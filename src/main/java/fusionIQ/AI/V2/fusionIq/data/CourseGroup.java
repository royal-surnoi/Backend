package fusionIQ.AI.V2.fusionIq.data;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class CourseGroup {

    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupName;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;



    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public CourseGroup(Long id, String groupName, User teacher, LocalDateTime createdAt) {
        this.id = id;
        this.groupName = groupName;
        this.teacher = teacher;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CourseGroup{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", teacher=" + teacher +
                ", createdAt=" + createdAt +
                '}';
    }

    public CourseGroup() {
        this.createdAt = LocalDateTime.now();
    }
}
