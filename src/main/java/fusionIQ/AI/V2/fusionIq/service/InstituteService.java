package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.repository.InstituteRepo;
import fusionIQ.AI.V2.fusionIq.repository.InstituteStudentRepo;
import fusionIQ.AI.V2.fusionIq.repository.InstituteTeacherRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class InstituteService {

    @Autowired
    private InstituteRepo instituteRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private InstituteTeacherRepo instituteTeacherRepo;

    @Autowired
    private InstituteStudentRepo instituteStudentRepo;


    @Autowired
    private UserRepo userRepo; // Add User repository to fetch the User

    @PersistenceContext
    private EntityManager entityManager;

    // Save organisation
    public Institute registerInstitute(String instituteName, String location, String principalName,
                                       String registrationNo, String instituteType, String board,
                                       String email, String addPassword, String confirmPassword,
                                       String pincode, Long userId, MultipartFile image) {

        // Fetch the User by ID
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Institute institute = new Institute();
        institute.setInstitute_Name(instituteName);
        institute.setLocation(location);
        institute.setPrincipal_name(principalName);
        institute.setInstitute_Registration_No(registrationNo);
        institute.setInstituteType(instituteType);
        institute.setBoard(board);
        institute.setEmail(email);
        institute.setAddPassword(addPassword);
        institute.setConfirmPassword(confirmPassword);
        institute.setPincode(pincode);
        institute.setUser(user); // Associate with the User

        try {
            if (image != null && !image.isEmpty()) {
                institute.setImage(image.getBytes());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to process image file", e);
        }

        Institute savedInstitute = instituteRepo.save(institute);

        // Send an email notification
        sendEmailNotification(email, instituteName);

        return savedInstitute;
    }

    private void sendEmailNotification(String toEmail, String instituteName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Institute Registration Successful");
            message.setText("Dear User,\n\nYour institute \"" + instituteName +
                    "\" has been successfully registered.\n\nThank you,\nFusionIQ Team");

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + toEmail);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send email notification: " + e.getMessage(), e);
        }
    }


    // Retrieve all organisations
    public List<Institute> getAllinstitutes() {
        return instituteRepo.findAll();
    }

    public boolean updateTeacherStatusByInstitute(Long instituteId, String teacherId, InstituteTeacher.Status status) {
        // Validate inputs
        if (instituteId == null || teacherId == null || status == null) {
            throw new IllegalArgumentException("Institute ID, Teacher ID, and status must be provided.");
        }

        // Check if the teacher exists for the given institute
        Optional<InstituteTeacher> optionalTeacher = instituteTeacherRepo.findByTeacherIdAndInstituteId(teacherId, instituteId);

        if (optionalTeacher.isPresent()) {
            InstituteTeacher teacher = optionalTeacher.get();

            // Update the teacher's status
            teacher.setStatus(status);
            instituteTeacherRepo.save(teacher); // Save the updated entity

            return true; // Successfully updated
        } else {
            return false; // Teacher not found for the given institute
        }
    }


    public boolean updateStudentStatusByinstitute(Long instituteId, Long studentId, InstituteStudent.Status status) {
        Optional<InstituteStudent> optionalStudent = instituteStudentRepo.findByIdAndInstituteId(studentId, instituteId);

        if (optionalStudent.isPresent()) {
            InstituteStudent student = optionalStudent.get();
            student.setStatus(status);
            instituteStudentRepo.save(student);
            return true;
        }

        return false;
    }

    public Institute getinstituteById(Long id) {
        return instituteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Institute not found for ID: " + id));
    }
    public List<Institute> getInstitutesByUserId(Long userId) {
        return instituteRepo.findByUserId(userId);
    }

    public Institute updateInstituteDetails(Long id, String contactNo, String description, byte[] profileImage, int establishedIn) {
        Institute institute = instituteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Institute not found with ID: " + id));

        // Update only the specified fields
        institute.setContactNo(contactNo);
        institute.setDescription(description);
        if (profileImage != null) {
            institute.setProfileImage(profileImage);
        }
        institute.setEstablishedIn(establishedIn);

        return instituteRepo.save(institute);
    }

    public List<Institute> getInstitutesByCriteria(String pincode, String institute_Name, String instituteType, String location, Integer establishedIn) {
        return instituteRepo.findInstitutesByCriteria(pincode, institute_Name, instituteType, location, establishedIn);
    }

    public Institute updateInstitute(Long id, String instituteName, String location, String principalName,
                                     String registrationNo, String instituteType, String board,
                                     String email, String addPassword, String confirmPassword,
                                     String pincode, String contactNo, String description, int establishedIn,
                                     MultipartFile profileImage) {

        // Fetch the Institute by ID
        Institute institute = instituteRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Institute not found with ID: " + id));

        // Update fields
        institute.setInstitute_Name(instituteName);
        institute.setLocation(location);
        institute.setPrincipal_name(principalName);
        institute.setInstitute_Registration_No(registrationNo);
        institute.setInstituteType(instituteType);
        institute.setBoard(board);
        institute.setEmail(email);
        institute.setAddPassword(addPassword);
        institute.setConfirmPassword(confirmPassword);
        institute.setPincode(pincode);
        institute.setContactNo(contactNo);
        institute.setDescription(description);
        institute.setEstablishedIn(establishedIn);

        // Update profile image
        try {
            if (profileImage != null && !profileImage.isEmpty()) {
                institute.setProfileImage(profileImage.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to process profile image", e);
        }

        // Save updated institute
        Institute savedInstitute = instituteRepo.save(institute);

        return savedInstitute;
    }

    // Get data by InstituteStudent ID
    public Map<String, Object> getByInstituteStudentId(long id) {
        InstituteStudent student = instituteStudentRepo.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));

        Map<String, Object> response = new HashMap<>();
        User user = student.getUser();
        Institute institute = student.getInstitute();
        PersonalDetails personalDetails = user.getPersonalDetails();

        response.put("Name", user.getName());
        response.put("Age", personalDetails.getAge());
        response.put("Interests", personalDetails.getInterests());
        response.put("Skills", personalDetails.getSkills());
        response.put("About", personalDetails.getUserDescription());
        response.put("UserId", user.getId());

        response.put("Class", student.getStudentClass());
        response.put("Section", student.getSection());
        response.put("RegistrationNo", student.getInstituteRegistrationNo());
        response.put("InstituteName", institute.getInstitute_Name());
        response.put("InstituteLocation", institute.getLocation());
        response.put("InstituteId", institute.getId());
        response.put("Id", student.getId());
        response.put("userImage", user.getUserImage());

        return response;
    }

    // Get data by InstituteTeacher ID
    public Map<String, Object> getByInstituteTeacherId(long id) {
        InstituteTeacher teacher = instituteTeacherRepo.findById(id).orElseThrow(() -> new RuntimeException("Teacher not found"));

        Map<String, Object> response = new HashMap<>();
        User user = teacher.getUser();
        Institute institute = teacher.getInstitute();
        PersonalDetails personalDetails = user.getPersonalDetails();

        response.put("Name", user.getName());
        response.put("Age", personalDetails.getAge());
        response.put("Interests", personalDetails.getInterests());
        response.put("Skills", personalDetails.getSkills());
        response.put("About", personalDetails.getUserDescription());
        response.put("UserId", user.getId());

        response.put("TeacherId", teacher.getTeacherId());
        response.put("Subject", teacher.getSubject());
        response.put("InstituteName", institute.getInstitute_Name());
        response.put("InstituteLocation", institute.getLocation());
        response.put("InstituteId", institute.getId());
        response.put("Id", teacher.getId());
        response.put("userImage", user.getUserImage());


        return response;
    }

    public List<Map<String, Object>> getAllRolesWithInstitutesByUserId(Long userId) {
        List<Map<String, Object>> result = new ArrayList<>();

        // Get institutes for user
        List<Institute> institutes = entityManager.createQuery(
                        "SELECT i FROM Institute i WHERE i.user.id = :userId", Institute.class)
                .setParameter("userId", userId)
                .getResultList();

        for (Institute institute : institutes) {
            Map<String, Object> instituteMap = new HashMap<>();
            instituteMap.put("id", institute.getId());
            instituteMap.put("role", "institute");
            instituteMap.put("instituteName", institute.getInstitute_Name());
            instituteMap.put("Image",institute.getProfileImage());
            result.add(instituteMap);
        }

        // Get students for user
        List<InstituteStudent> students = entityManager.createQuery(
                        "SELECT s FROM InstituteStudent s JOIN FETCH s.institute WHERE s.user.id = :userId and status = 'ACCEPTED'",
                        InstituteStudent.class)
                .setParameter("userId", userId)
                .getResultList();

        for (InstituteStudent student : students) {
            Map<String, Object> studentMap = new HashMap<>();
            studentMap.put("id", student.getId());
            studentMap.put("role", "student");
            studentMap.put("instituteName", student.getInstitute().getInstitute_Name());
            studentMap.put("Image",student.getUserImage());
            result.add(studentMap);
        }

        // Get teachers for user
        List<InstituteTeacher> teachers = entityManager.createQuery(
                        "SELECT t FROM InstituteTeacher t JOIN FETCH t.institute WHERE t.user.id = :userId and status = 'ACCEPTED'",
                        InstituteTeacher.class)
                .setParameter("userId", userId)
                .getResultList();

        for (InstituteTeacher teacher : teachers) {
            Map<String, Object> teacherMap = new HashMap<>();
            teacherMap.put("id", teacher.getId());
            teacherMap.put("role", "teacher");
            teacherMap.put("instituteName", teacher.getInstitute().getInstitute_Name());
            teacherMap.put("Image",teacher.getUserImage());
            result.add(teacherMap);
        }

        return result;
    }
}
