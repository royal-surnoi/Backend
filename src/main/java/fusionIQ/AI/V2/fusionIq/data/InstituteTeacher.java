package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Arrays;

@Entity
public class InstituteTeacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String teacher_Name;
    private String teacherId;
    private String subject;
    private String institute_Type;
    private String location;
    private String password;
    private String confirm_Password;
    private String email;
    private String otp;
    private String verified_Otp;
    private String pincode;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        ACCEPTED,
        REJECTED,
        PENDING
    }

    @ManyToOne
    @JoinColumn(name = "institute_id")
    private Institute institute;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = true)
    private byte[] userImage;



    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getVerified_Otp() {
        return verified_Otp;
    }

    public void setVerified_Otp(String verified_Otp) {
        this.verified_Otp = verified_Otp;
    }
// Getters and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTeacher_Name() {
        return teacher_Name;
    }

    public void setTeacher_Name(String teacher_Name) {
        this.teacher_Name = teacher_Name;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getInstitute_Type() {
        return institute_Type;
    }

    public void setInstitute_Type(String institute_Type) {
        this.institute_Type = institute_Type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirm_Password() {
        return confirm_Password;
    }

    public void setConfirm_Password(String confirm_Password) {
        this.confirm_Password = confirm_Password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    // Constructors

    public InstituteTeacher() {
    }

    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    public InstituteTeacher(long id, String teacher_Name, String teacherId, String subject, String institute_Type, String location, String password, String confirm_Password, String email, String otp, String verified_Otp, String pincode, Status status, Institute institute, User user, byte[] userImage) {
        this.id = id;
        this.teacher_Name = teacher_Name;
        this.teacherId = teacherId;
        this.subject = subject;
        this.institute_Type = institute_Type;
        this.location = location;
        this.password = password;
        this.confirm_Password = confirm_Password;
        this.email = email;
        this.otp = otp;
        this.verified_Otp = verified_Otp;
        this.pincode = pincode;
        this.status = status;
        this.institute = institute;
        this.user = user;
        this.userImage = userImage;
    }

    // toString method


    @Override
    public String toString() {
        return "InstituteTeacher{" +
                "id=" + id +
                ", teacher_Name='" + teacher_Name + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", subject='" + subject + '\'' +
                ", institute_Type='" + institute_Type + '\'' +
                ", location='" + location + '\'' +
                ", password='" + password + '\'' +
                ", confirm_Password='" + confirm_Password + '\'' +
                ", email='" + email + '\'' +
                ", otp='" + otp + '\'' +
                ", verified_Otp='" + verified_Otp + '\'' +
                ", pincode='" + pincode + '\'' +
                ", status=" + status +
                ", institute=" + institute +
                ", user=" + user +
                ", userImage=" + Arrays.toString(userImage) +
                '}';
    }
}