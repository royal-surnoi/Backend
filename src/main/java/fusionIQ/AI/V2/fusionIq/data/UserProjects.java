package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

import java.util.Arrays;

import java.util.Date;

@Entity

public class UserProjects {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(name = "project_title")

    private String projectName;

    @Column(name = "project_client_name")

    private String client;

    @Column(name="project_description")

    private String projectDescription;

    @Column(name = "project_start_date")

    private Date startDate;

    @Column(name = "project_end_date")

    private Date endDate;

    @Column(name = "technologies_used")

    private String skillsUsed;

    @Column(name = "project_url")

    private String projectLink;

    @Lob

    @Column(name = "project_image", columnDefinition = "LONGBLOB", nullable = true)

    private byte[] projectImage;

    @Lob

    @Column(name = "project_video", columnDefinition = "LONGBLOB", nullable = true)

    private byte[] projectVideo;

    @ManyToOne

    @JoinColumn(name = "user_id", nullable = false)

    private User user;

    public Long getId() {

        return id;

    }

    public void setId(Long id) {

        this.id = id;

    }

    public String getProjectName() {

        return projectName;

    }

    public void setProjectName(String projectName) {

        this.projectName = projectName;

    }

    public String getClient() {

        return client;

    }

    public void setClient(String client) {

        this.client = client;

    }

    public Date getStartDate() {

        return startDate;

    }

    public void setStartDate(Date startDate) {

        this.startDate = startDate;

    }

    public Date getEndDate() {

        return endDate;

    }

    public void setEndDate(Date endDate) {

        this.endDate = endDate;

    }

    public String getSkillsUsed() {

        return skillsUsed;

    }

    public void setSkillsUsed(String skillsUsed) {

        this.skillsUsed = skillsUsed;

    }

    public String getProjectLink() {

        return projectLink;

    }

    public void setProjectLink(String projectLink) {

        this.projectLink = projectLink;

    }

    public byte[] getProjectImage() {

        return projectImage;

    }

    public void setProjectImage(byte[] projectImage) {

        this.projectImage = projectImage;

    }

    public byte[] getProjectVideo() {

        return projectVideo;

    }

    public void setProjectVideo(byte[] projectVideo) {

        this.projectVideo = projectVideo;

    }

    public User getUser() {

        return user;

    }

    public void setUser(User user) {

        this.user = user;

    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public UserProjects(Long id, String projectName, String projectDescription, String client, Date startDate, Date endDate, String skillsUsed, String projectLink, byte[] projectImage, byte[] projectVideo, User user) {

        this.id = id;

        this.projectName = projectName;

        this.client = client;

        this.projectDescription = projectDescription;

        this.startDate = startDate;

        this.endDate = endDate;

        this.skillsUsed = skillsUsed;

        this.projectLink = projectLink;

        this.projectImage = projectImage;

        this.projectVideo = projectVideo;

        this.user = user;

    }

    public UserProjects() {

    }

    @Override

    public String toString() {

        return "UserProjects{" +

                "id=" + id +

                ", projectName='" + projectName + '\'' +

                ", client='" + client + '\'' +

                ", projectDescription='" + projectDescription + '\''+

                ", startDate=" + startDate +

                ", endDate=" + endDate +

                ", skillsUsed='" + skillsUsed + '\'' +

                ", projectLink='" + projectLink + '\'' +

                ", projectImage=" + Arrays.toString(projectImage) +

                ", projectVideo=" + Arrays.toString(projectVideo) +

                ", user=" + user +

                '}';

    }

}


