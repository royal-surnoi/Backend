package fusionIQ.AI.V2.fusionIq.data;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class InstituteStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;
    private String studentName;
    private String instituteRegistrationNo;
    private String studentClass;
    private String section;
    private String institute_Type;
    private String location;
    private String password;
    private String confirmPassword;
    private String email;
    private String pincode;
    private String otp;
    private String verified_Otp;

    @Lob
    @Column(columnDefinition = "LONGBLOB", nullable = true)
    private byte[] userImage;

    @Enumerated(EnumType.STRING)
    private InstituteStudent.Status status;

    public enum Status {
        ACCEPTED,
        REJECTED,
        PENDING
    }

    @ManyToOne
    @JoinColumn(name = "institute_id")
    private Institute institute;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public InstituteStudent(long id, String studentName, String instituteRegistrationNo, String studentClass, String section, String institute_Type, String location, String password, String confirmPassword, String email, String pincode, String otp, String verified_Otp, byte[] userImage, Status status, Institute institute, User user) {
        this.id = id;
        this.studentName = studentName;
        this.instituteRegistrationNo = instituteRegistrationNo;
        this.studentClass = studentClass;
        this.section = section;
        this.institute_Type = institute_Type;
        this.location = location;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.pincode = pincode;
        this.otp = otp;
        this.verified_Otp = verified_Otp;
        this.userImage = userImage;
        this.status = status;
        this.institute = institute;
        this.user = user;
    }

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

    public InstituteStudent() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getInstituteRegistrationNo() {
        return instituteRegistrationNo;
    }

    public void setInstituteRegistrationNo(String instituteRegistrationNo) {
        this.instituteRegistrationNo = instituteRegistrationNo;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
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

    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    @Override
    public String toString() {
        return "InstituteStudent{" +
                "id=" + id +
                ", studentName='" + studentName + '\'' +
                ", instituteRegistrationNo='" + instituteRegistrationNo + '\'' +
                ", studentClass='" + studentClass + '\'' +
                ", section='" + section + '\'' +
                ", institute_Type='" + institute_Type + '\'' +
                ", location='" + location + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", email='" + email + '\'' +
                ", pincode='" + pincode + '\'' +
                ", otp='" + otp + '\'' +
                ", verified_Otp='" + verified_Otp + '\'' +
                ", userImage=" + Arrays.toString(userImage) +
                ", status=" + status +
                ", institute=" + institute +
                ", user=" + user +
                '}';
    }
}