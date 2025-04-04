package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String courseTitle;

    @Column(columnDefinition = "LONGTEXT")
    private String courseDescription;
    private String courseLanguage;

    private Level level;

    private String courseDuration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Submission> submissions;
    @ManyToOne
    @JoinColumn(name = "user_Id")
    private User user;
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Video> videos;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Review> reviews;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Project> projects;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    private List<CourseTools> courseTools;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<SubmitProject> submitProjects;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Announcement> announcements;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Quiz> quizzes;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Answer> answers;



    private Long courseFee;
    private Long discountFee;
    private Long discountPercentage;
    private String currency;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime promoCodeExpiration;

    private String level_1;
    private String level_2;
    private String level_3;
    private String level_4;
    private String level_5;
    private String level_6;
    private String level_7;
    private String level_8;
    private String promoCode;
    private String courseType;
    private String coursePercentage;
    private String courseTerm;

    private float courseRating;

    @Lob
    @Column(length = 100000)
    private byte[] courseImage;

    @Lob
    @Column(length = 100000)
    private byte[] courseDocument;


    public enum Level {
        Beginner,
        Intermediate,
        Advanced
    }

    public Course() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.promoCodeExpiration = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseLanguage() {
        return courseLanguage;
    }

    public void setCourseLanguage(String courseLanguage) {
        this.courseLanguage = courseLanguage;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(String courseDuration) {
        this.courseDuration = courseDuration;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public List<CourseTools> getCourseTools() {
        return courseTools;
    }

    public void setCourseTools(List<CourseTools> courseTools) {
        this.courseTools = courseTools;
    }

    public List<SubmitProject> getSubmitProjects() {
        return submitProjects;
    }

    public void setSubmitProjects(List<SubmitProject> submitProjects) {
        this.submitProjects = submitProjects;
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Long getCourseFee() {
        return courseFee;
    }

    public void setCourseFee(Long courseFee) {
        this.courseFee = courseFee;
    }

    public Long getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Long discountFee) {
        this.discountFee = discountFee;
    }

    public Long getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Long discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getPromoCodeExpiration() {
        return promoCodeExpiration;
    }

    public void setPromoCodeExpiration(LocalDateTime promoCodeExpiration) {
        this.promoCodeExpiration = promoCodeExpiration;
    }

    public String getLevel_1() {
        return level_1;
    }

    public void setLevel_1(String level_1) {
        this.level_1 = level_1;
    }

    public String getLevel_2() {
        return level_2;
    }

    public void setLevel_2(String level_2) {
        this.level_2 = level_2;
    }

    public String getLevel_3() {
        return level_3;
    }

    public void setLevel_3(String level_3) {
        this.level_3 = level_3;
    }

    public String getLevel_4() {
        return level_4;
    }

    public void setLevel_4(String level_4) {
        this.level_4 = level_4;
    }

    public String getLevel_5() {
        return level_5;
    }

    public void setLevel_5(String level_5) {
        this.level_5 = level_5;
    }

    public String getLevel_6() {
        return level_6;
    }

    public void setLevel_6(String level_6) {
        this.level_6 = level_6;
    }

    public String getLevel_7() {
        return level_7;
    }

    public void setLevel_7(String level_7) {
        this.level_7 = level_7;
    }

    public String getLevel_8() {
        return level_8;
    }

    public void setLevel_8(String level_8) {
        this.level_8 = level_8;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCoursePercentage() {
        return coursePercentage;
    }

    public void setCoursePercentage(String coursePercentage) {
        this.coursePercentage = coursePercentage;
    }

    public String getCourseTerm() {
        return courseTerm;
    }

    public void setCourseTerm(String courseTerm) {
        this.courseTerm = courseTerm;
    }

    public float getCourseRating() {
        return courseRating;
    }

    public void setCourseRating(float courseRating) {
        this.courseRating = courseRating;
    }

    public byte[] getCourseImage() {
        return courseImage;
    }

    public void setCourseImage(byte[] courseImage) {
        this.courseImage = courseImage;
    }

    public byte[] getCourseDocument() {
        return courseDocument;
    }

    public void setCourseDocument(byte[] courseDocument) {
        this.courseDocument = courseDocument;
    }

    public Course(long id, String courseTitle, String courseDescription, String courseLanguage, Level level, String courseDuration, LocalDateTime createdAt, LocalDateTime updatedAt, List<Lesson> lessons, List<Submission> submissions, User user, List<Enrollment> enrollments, List<Video> videos, List<Review> reviews, List<Project> projects, List<Assignment> assignments, List<CourseTools> courseTools, List<SubmitProject> submitProjects, List<Announcement> announcements, List<Quiz> quizzes, List<Answer> answers, Long courseFee, Long discountFee, Long discountPercentage, String currency, LocalDateTime promoCodeExpiration, String level_1, String level_2, String level_3, String level_4, String level_5, String level_6, String level_7, String level_8, String promoCode, String courseType, String coursePercentage, String courseTerm, float courseRating, byte[] courseImage, byte[] courseDocument) {
        this.id = id;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.courseLanguage = courseLanguage;
        this.level = level;
        this.courseDuration = courseDuration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lessons = lessons;
        this.submissions = submissions;
        this.user = user;
        this.enrollments = enrollments;
        this.videos = videos;
        this.reviews = reviews;
        this.projects = projects;
        this.assignments = assignments;
        this.courseTools = courseTools;
        this.submitProjects = submitProjects;
        this.announcements = announcements;
        this.quizzes = quizzes;
        this.answers = answers;
        this.courseFee = courseFee;
        this.discountFee = discountFee;
        this.discountPercentage = discountPercentage;
        this.currency = currency;
        this.promoCodeExpiration = promoCodeExpiration;
        this.level_1 = level_1;
        this.level_2 = level_2;
        this.level_3 = level_3;
        this.level_4 = level_4;
        this.level_5 = level_5;
        this.level_6 = level_6;
        this.level_7 = level_7;
        this.level_8 = level_8;
        this.promoCode = promoCode;
        this.courseType = courseType;
        this.coursePercentage = coursePercentage;
        this.courseTerm = courseTerm;
        this.courseRating = courseRating;
        this.courseImage = courseImage;
        this.courseDocument = courseDocument;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseTitle='" + courseTitle + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                ", courseLanguage='" + courseLanguage + '\'' +
                ", level=" + level +
                ", courseDuration='" + courseDuration + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", lessons=" + lessons +
                ", submissions=" + submissions +
                ", user=" + user +
                ", enrollments=" + enrollments +
                ", videos=" + videos +
                ", reviews=" + reviews +
                ", projects=" + projects +
                ", assignments=" + assignments +
                ", courseTools=" + courseTools +
                ", submitProjects=" + submitProjects +
                ", announcements=" + announcements +
                ", quizzes=" + quizzes +
                ", answers=" + answers +
                ", courseFee=" + courseFee +
                ", discountFee=" + discountFee +
                ", discountPercentage=" + discountPercentage +
                ", currency='" + currency + '\'' +
                ", promoCodeExpiration=" + promoCodeExpiration +
                ", level_1='" + level_1 + '\'' +
                ", level_2='" + level_2 + '\'' +
                ", level_3='" + level_3 + '\'' +
                ", level_4='" + level_4 + '\'' +
                ", level_5='" + level_5 + '\'' +
                ", level_6='" + level_6 + '\'' +
                ", level_7='" + level_7 + '\'' +
                ", level_8='" + level_8 + '\'' +
                ", promoCode='" + promoCode + '\'' +
                ", courseType='" + courseType + '\'' +
                ", coursePercentage='" + coursePercentage + '\'' +
                ", courseTerm='" + courseTerm + '\'' +
                ", courseRating=" + courseRating +
                ", courseImage=" + Arrays.toString(courseImage) +
                ", courseDocument=" + Arrays.toString(courseDocument) +
                '}';
    }

    public Course(Long id) {
        this.id = id;
    }
}

