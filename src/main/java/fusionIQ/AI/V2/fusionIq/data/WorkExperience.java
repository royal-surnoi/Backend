package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class WorkExperience implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String workCompanyName;
    private String workStartDate;
    private String workEndDate;

    @Column(length = 1500)
    private String workDescription;
    private String workRole;
    private Boolean currentlyWorking;
    private Double yearsOfExperience;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public WorkExperience() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkCompanyName() {
        return workCompanyName;
    }

    public void setWorkCompanyName(String workCompanyName) {
        this.workCompanyName = workCompanyName;
    }

    public String getWorkStartDate() {
        return workStartDate;
    }

    public void setWorkStartDate(String workStartDate) {
        this.workStartDate = workStartDate;
    }

    public String getWorkEndDate() {
        return workEndDate;
    }

    public void setWorkEndDate(String workEndDate) {
        this.workEndDate = workEndDate;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public String getWorkRole() {
        return workRole;
    }

    public void setWorkRole(String workRole) {
        this.workRole = workRole;
    }

    public Boolean getCurrentlyWorking() {
        return currentlyWorking;
    }

    public void setCurrentlyWorking(Boolean currentlyWorking) {
        this.currentlyWorking = currentlyWorking;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public Double getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Double yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public WorkExperience(Long id, String workCompanyName, String workStartDate, String workEndDate, String workDescription, String workRole, Boolean currentlyWorking,Double yearsOfExperience, LocalDateTime createdAt, User user) {
        this.id = id;
        this.workCompanyName = workCompanyName;
        this.workStartDate = workStartDate;
        this.workEndDate = workEndDate;
        this.workDescription = workDescription;
        this.workRole = workRole;
        this.currentlyWorking = currentlyWorking;
        this.createdAt = createdAt;
        this.user = user;
        this.yearsOfExperience = yearsOfExperience;
    }

    @Override
    public String toString() {
        return "WorkExperience{" +
                "id=" + id +
                ", workCompanyName='" + workCompanyName + '\'' +
                ", workStartDate='" + workStartDate + '\'' +
                ", workEndDate='" + workEndDate + '\'' +
                ", workDescription='" + workDescription + '\'' +
                ", workRole='" + workRole + '\'' +
                ", currentlyWorking=" + currentlyWorking +
                ", createdAt=" + createdAt +
                ", yearsOfExperience=" + yearsOfExperience +
                ", user=" + user +
                '}';
    }

}
