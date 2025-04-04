package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String searchContent;
    private String courseContent;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String jobContent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime searchedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    public String getCourseContent() {
        return courseContent;
    }

    public void setCourseContent(String courseContent) {
        this.courseContent = courseContent;
    }

    public String getJobContent() {
        return jobContent;
    }

    public void setJobContent(String jobContent) {
        this.jobContent = jobContent;
    }

    public LocalDateTime getSearchedAt() {
        return searchedAt;
    }

    public void setSearchedAt(LocalDateTime searchedAt) {
        this.searchedAt = searchedAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Search(Long id, String searchContent, String courseContent, String jobContent, LocalDateTime searchedAt, User user) {
        this.id = id;
        this.searchContent = searchContent;
        this.courseContent = courseContent;
        this.jobContent = jobContent;
        this.searchedAt = searchedAt;
        this.user = user;
    }

    public Search() {
        this.searchedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Search{" +
                "id=" + id +
                ", searchContent='" + searchContent + '\'' +
                ", courseContent='" + courseContent + '\'' +
                ", jobContent='" + jobContent + '\'' +
                ", searchedAt=" + searchedAt +
                ", user=" + user +
                '}';
    }
}