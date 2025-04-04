package fusionIQ.AI.V2.fusionIq.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Institute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String institute_Name;
    private String location;
    private String principal_name;
    private String institute_Registration_No;
    @Column(name = "institute_Type")
    private String instituteType;

    private String board;
    private String email;

    @Lob
    @Column(length = 100000)
    private byte[] image;

    private String addPassword;
    private String confirmPassword;
    private String pincode;

    // New fields
    private String contactNo;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @JsonIgnore
    @Lob
    @Column(length = 100000)
    private byte[] profileImage;

    private int establishedIn;

    @OneToMany(mappedBy = "institute", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<InstituteTeacher> teachers = new ArrayList<>();

    @OneToMany(mappedBy = "institute", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<InstituteStudent> students = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInstitute_Name() {
        return institute_Name;
    }

    public void setInstitute_Name(String institute_Name) {
        this.institute_Name = institute_Name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrincipal_name() {
        return principal_name;
    }

    public void setPrincipal_name(String principal_name) {
        this.principal_name = principal_name;
    }

    public String getInstitute_Registration_No() {
        return institute_Registration_No;
    }

    public void setInstitute_Registration_No(String institute_Registration_No) {
        this.institute_Registration_No = institute_Registration_No;
    }

    public String getInstituteType() {
        return instituteType;
    }

    public void setInstituteType(String instituteType) {
        this.instituteType = instituteType;
    }
    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getAddPassword() {
        return addPassword;
    }

    public void setAddPassword(String addPassword) {
        this.addPassword = addPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public List<InstituteTeacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<InstituteTeacher> teachers) {
        this.teachers = teachers;
    }

    public List<InstituteStudent> getStudents() {
        return students;
    }

    public void setStudents(List<InstituteStudent> students) {
        this.students = students;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public int getEstablishedIn() {
        return establishedIn;
    }

    public void setEstablishedIn(int establishedIn) {
        this.establishedIn = establishedIn;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Institute(long id, String institute_Name, String location, String principal_name, String institute_Registration_No, String instituteType, String board, String email, byte[] image, String addPassword, String confirmPassword, String pincode, String contactNo, String description, byte[] profileImage, int establishedIn, List<InstituteTeacher> teachers, List<InstituteStudent> students, User user) {
        this.id = id;
        this.institute_Name = institute_Name;
        this.location = location;
        this.principal_name = principal_name;
        this.institute_Registration_No = institute_Registration_No;
        this.instituteType = instituteType;
        this.board = board;
        this.email = email;
        this.image = image;
        this.addPassword = addPassword;
        this.confirmPassword = confirmPassword;
        this.pincode = pincode;
        this.contactNo = contactNo;
        this.description = description;
        this.profileImage = profileImage;
        this.establishedIn = establishedIn;
        this.teachers = teachers;
        this.students = students;
        this.user = user;
    }

    public Institute() {
    }

    @Override
    public String toString() {
        return "Institute{" +
                "id=" + id +
                ", institute_Name='" + institute_Name + '\'' +
                ", location='" + location + '\'' +
                ", principal_name='" + principal_name + '\'' +
                ", institute_Registration_No='" + institute_Registration_No + '\'' +
                ", instituteType='" + instituteType + '\'' +
                ", board='" + board + '\'' +
                ", email='" + email + '\'' +
                ", image=" + Arrays.toString(image) +
                ", addPassword='" + addPassword + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", pincode='" + pincode + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", description='" + description + '\'' +
                ", profileImage=" + Arrays.toString(profileImage) +
                ", establishedIn=" + establishedIn +
                ", teachers=" + teachers +
                ", students=" + students +
                ", user=" + user +
                '}';
    }
}
