package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Education implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public enum TypeOfDegree {
        BTECH, DEGREE
    }

    @Enumerated(EnumType.STRING)
    private TypeOfDegree TypeOfDegree;
    private String schoolStatus;
    private String schoolName;
    private String schoolPercentage;
    private Integer schoolYearOfPassout;
    private String schoolEducationBoard;
    private Integer schoolYearOfJoining;
    private String pursuingClass;
    private String intermediateDiploma;


    private String intermediateStatus;

    private String intermediateCollegeName;

    private String intermediateCollegeSpecialization;
    private String intermediateCollegePercentage;
    private Integer intermediateYearOfPassout;
    private String intermediateEducationBoard;
    private Integer intermediateYearOfJoining;


    private String graduationStatus;

    private String graduationCollegeName;
    private String graduationCollegeSpecialization;
    private String graduationCollegePercentage;
    private Integer graduationYearOfPassout;
    private Integer graduationYearOfJoining;


    private String postGraduateStatus;

    private String postGraduateCollegeName;
    private String postGraduateCollegeSpecialization;
    private String postGraduateCollegePercentage;
    private Integer postGraduateYearOfPassout;
    private Integer postGraduateYearOfJoining;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;



    public Education() {
        this.createdAt=LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeOfDegree getTypeOfDegree() {
        return TypeOfDegree;
    }

    public void setTypeOfDegree(TypeOfDegree TypeOfDegree) {
        this.TypeOfDegree = TypeOfDegree;
    }

    public String getSchoolStatus() {
        return schoolStatus;
    }

    public void setSchoolStatus(String schoolStatus) {
        this.schoolStatus = schoolStatus;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolPercentage() {
        return schoolPercentage;
    }

    public void setSchoolPercentage(String schoolPercentage) {
        this.schoolPercentage = schoolPercentage;
    }

    public Integer getSchoolYearOfPassout() {
        return schoolYearOfPassout;
    }

    public void setSchoolYearOfPassout(Integer schoolYearOfPassout) {
        this.schoolYearOfPassout = schoolYearOfPassout;
    }

    public String getSchoolEducationBoard() {
        return schoolEducationBoard;
    }

    public void setSchoolEducationBoard(String schoolEducationBoard) {
        this.schoolEducationBoard = schoolEducationBoard;
    }

    public Integer getSchoolYearOfJoining() {
        return schoolYearOfJoining;
    }

    public void setSchoolYearOfJoining(Integer schoolYearOfJoining) {
        this.schoolYearOfJoining = schoolYearOfJoining;
    }

    public String getPursuingClass() {
        return pursuingClass;
    }

    public void setPursuingClass(String pursuingClass) {
        this.pursuingClass = pursuingClass;
    }

    public String getIntermediateDiploma() {
        return intermediateDiploma;
    }

    public void setIntermediateDiploma(String intermediateDiploma) {
        this.intermediateDiploma = intermediateDiploma;
    }

    public String getIntermediateStatus() {
        return intermediateStatus;
    }

    public void setIntermediateStatus(String intermediateStatus) {
        this.intermediateStatus = intermediateStatus;
    }

    public String getIntermediateCollegeName() {
        return intermediateCollegeName;
    }

    public void setIntermediateCollegeName(String intermediateCollegeName) {
        this.intermediateCollegeName = intermediateCollegeName;
    }

    public String getIntermediateCollegeSpecialization() {
        return intermediateCollegeSpecialization;
    }

    public void setIntermediateCollegeSpecialization(String intermediateCollegeSpecialization) {
        this.intermediateCollegeSpecialization = intermediateCollegeSpecialization;
    }

    public String getIntermediateCollegePercentage() {
        return intermediateCollegePercentage;
    }

    public void setIntermediateCollegePercentage(String intermediateCollegePercentage) {
        this.intermediateCollegePercentage = intermediateCollegePercentage;
    }

    public Integer getIntermediateYearOfPassout() {
        return intermediateYearOfPassout;
    }

    public void setIntermediateYearOfPassout(Integer intermediateYearOfPassout) {
        this.intermediateYearOfPassout = intermediateYearOfPassout;
    }

    public String getIntermediateEducationBoard() {
        return intermediateEducationBoard;
    }

    public void setIntermediateEducationBoard(String intermediateEducationBoard) {
        this.intermediateEducationBoard = intermediateEducationBoard;
    }

    public Integer getIntermediateYearOfJoining() {
        return intermediateYearOfJoining;
    }

    public void setIntermediateYearOfJoining(Integer intermediateYearOfJoining) {
        this.intermediateYearOfJoining = intermediateYearOfJoining;
    }

    public String getGraduationStatus() {
        return graduationStatus;
    }

    public void setGraduationStatus(String graduationStatus) {
        this.graduationStatus = graduationStatus;
    }

    public String getGraduationCollegeName() {
        return graduationCollegeName;
    }

    public void setGraduationCollegeName(String graduationCollegeName) {
        this.graduationCollegeName = graduationCollegeName;
    }

    public String getGraduationCollegeSpecialization() {
        return graduationCollegeSpecialization;
    }

    public void setGraduationCollegeSpecialization(String graduationCollegeSpecialization) {
        this.graduationCollegeSpecialization = graduationCollegeSpecialization;
    }

    public String getGraduationCollegePercentage() {
        return graduationCollegePercentage;
    }

    public void setGraduationCollegePercentage(String graduationCollegePercentage) {
        this.graduationCollegePercentage = graduationCollegePercentage;
    }

    public Integer getGraduationYearOfPassout() {
        return graduationYearOfPassout;
    }

    public void setGraduationYearOfPassout(Integer graduationYearOfPassout) {
        this.graduationYearOfPassout = graduationYearOfPassout;
    }

    public Integer getGraduationYearOfJoining() {
        return graduationYearOfJoining;
    }

    public void setGraduationYearOfJoining(Integer graduationYearOfJoining) {
        this.graduationYearOfJoining = graduationYearOfJoining;
    }

    public String getPostGraduateStatus() {
        return postGraduateStatus;
    }

    public void setPostGraduateStatus(String postGraduateStatus) {
        this.postGraduateStatus = postGraduateStatus;
    }

    public String getPostGraduateCollegeName() {
        return postGraduateCollegeName;
    }

    public void setPostGraduateCollegeName(String postGraduateCollegeName) {
        this.postGraduateCollegeName = postGraduateCollegeName;
    }

    public String getPostGraduateCollegeSpecialization() {
        return postGraduateCollegeSpecialization;
    }

    public void setPostGraduateCollegeSpecialization(String postGraduateCollegeSpecialization) {
        this.postGraduateCollegeSpecialization = postGraduateCollegeSpecialization;
    }

    public String getPostGraduateCollegePercentage() {
        return postGraduateCollegePercentage;
    }

    public void setPostGraduateCollegePercentage(String postGraduateCollegePercentage) {
        this.postGraduateCollegePercentage = postGraduateCollegePercentage;
    }

    public Integer getPostGraduateYearOfPassout() {
        return postGraduateYearOfPassout;
    }

    public void setPostGraduateYearOfPassout(Integer postGraduateYearOfPassout) {
        this.postGraduateYearOfPassout = postGraduateYearOfPassout;
    }

    public Integer getPostGraduateYearOfJoining() {
        return postGraduateYearOfJoining;
    }

    public void setPostGraduateYearOfJoining(Integer postGraduateYearOfJoining) {
        this.postGraduateYearOfJoining = postGraduateYearOfJoining;
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

    public Education(Long id,TypeOfDegree TypeOfDegree, String schoolStatus, String schoolName, String schoolPercentage, Integer schoolYearOfPassout, String schoolEducationBoard, Integer schoolYearOfJoining, String pursuingClass, String intermediateDiploma, String intermediateStatus, String intermediateCollegeName, String intermediateCollegeSpecialization, String intermediateCollegePercentage, Integer intermediateYearOfPassout, String intermediateEducationBoard, Integer intermediateYearOfJoining, String graduationStatus, String graduationCollegeName, String graduationCollegeSpecialization, String graduationCollegePercentage, Integer graduationYearOfPassout, Integer graduationYearOfJoining, String postGraduateStatus, String postGraduateCollegeName, String postGraduateCollegeSpecialization, String postGraduateCollegePercentage, Integer postGraduateYearOfPassout, Integer postGraduateYearOfJoining, LocalDateTime createdAt, User user) {
        this.id = id;
        this.TypeOfDegree = TypeOfDegree;
        this.schoolStatus = schoolStatus;
        this.schoolName = schoolName;
        this.schoolPercentage = schoolPercentage;
        this.schoolYearOfPassout = schoolYearOfPassout;
        this.schoolEducationBoard = schoolEducationBoard;
        this.schoolYearOfJoining = schoolYearOfJoining;
        this.pursuingClass = pursuingClass;
        this.intermediateDiploma = intermediateDiploma;
        this.intermediateStatus = intermediateStatus;
        this.intermediateCollegeName = intermediateCollegeName;
        this.intermediateCollegeSpecialization = intermediateCollegeSpecialization;
        this.intermediateCollegePercentage = intermediateCollegePercentage;
        this.intermediateYearOfPassout = intermediateYearOfPassout;
        this.intermediateEducationBoard = intermediateEducationBoard;
        this.intermediateYearOfJoining = intermediateYearOfJoining;
        this.graduationStatus = graduationStatus;
        this.graduationCollegeName = graduationCollegeName;
        this.graduationCollegeSpecialization = graduationCollegeSpecialization;
        this.graduationCollegePercentage = graduationCollegePercentage;
        this.graduationYearOfPassout = graduationYearOfPassout;
        this.graduationYearOfJoining = graduationYearOfJoining;
        this.postGraduateStatus = postGraduateStatus;
        this.postGraduateCollegeName = postGraduateCollegeName;
        this.postGraduateCollegeSpecialization = postGraduateCollegeSpecialization;
        this.postGraduateCollegePercentage = postGraduateCollegePercentage;
        this.postGraduateYearOfPassout = postGraduateYearOfPassout;
        this.postGraduateYearOfJoining = postGraduateYearOfJoining;
        this.createdAt = createdAt;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Education{" +
                "id=" + id +
                ", degree=" + TypeOfDegree +
                ", schoolStatus='" + schoolStatus + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", schoolPercentage='" + schoolPercentage + '\'' +
                ", schoolYearOfPassout=" + schoolYearOfPassout +
                ", schoolEducationBoard='" + schoolEducationBoard + '\'' +
                ", schoolYearOfJoining=" + schoolYearOfJoining +
                ", pursuingClass='" + pursuingClass + '\'' +
                ", intermediateDiploma='" + intermediateDiploma + '\'' +
                ", intermediateStatus='" + intermediateStatus + '\'' +
                ", intermediateCollegeName='" + intermediateCollegeName + '\'' +
                ", intermediateCollegeSpecialization='" + intermediateCollegeSpecialization + '\'' +
                ", intermediateCollegePercentage='" + intermediateCollegePercentage + '\'' +
                ", intermediateYearOfPassout=" + intermediateYearOfPassout +
                ", intermediateEducationBoard='" + intermediateEducationBoard + '\'' +
                ", intermediateYearOfJoining=" + intermediateYearOfJoining +
                ", graduationStatus='" + graduationStatus + '\'' +
                ", graduationCollegeName='" + graduationCollegeName + '\'' +
                ", graduationCollegeSpecialization='" + graduationCollegeSpecialization + '\'' +
                ", graduationCollegePercentage='" + graduationCollegePercentage + '\'' +
                ", graduationYearOfPassout=" + graduationYearOfPassout +
                ", graduationYearOfJoining=" + graduationYearOfJoining +
                ", postGraduateStatus='" + postGraduateStatus + '\'' +
                ", postGraduateCollegeName='" + postGraduateCollegeName + '\'' +
                ", postGraduateCollegeSpecialization='" + postGraduateCollegeSpecialization + '\'' +
                ", postGraduateCollegePercentage='" + postGraduateCollegePercentage + '\'' +
                ", postGraduateYearOfPassout=" + postGraduateYearOfPassout +
                ", postGraduateYearOfJoining=" + postGraduateYearOfJoining +
                ", createdAt=" + createdAt +
                ", user=" + user +
                '}';
    }




}