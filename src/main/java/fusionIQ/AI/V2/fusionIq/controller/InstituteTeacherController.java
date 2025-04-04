package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.InstituteTeacher;
import fusionIQ.AI.V2.fusionIq.data.User;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.InstituteTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/teachers")
public class InstituteTeacherController {

    @Autowired
    private InstituteTeacherService instituteTeacherService;
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/register")
    public ResponseEntity<?> registerTeacher(
            @RequestParam("teacherId") String teacherId,
            @RequestParam("teacher_Name") String teacherName,
            @RequestParam("subject") String subject,
            @RequestParam("institute_Type") String instituteType,
            @RequestParam("location") String location,
            @RequestParam("password") String password,
            @RequestParam("confirm_Password") String confirmPassword,
            @RequestParam("email") String email,
            @RequestParam("pincode") String pincode,
            @RequestParam("status") InstituteTeacher.Status status,
            @RequestParam("instituteId") Long instituteId,
            @RequestParam("userId") Long userId) {

        // Check for duplicate email and userId
        if (instituteTeacherService.isEmailExists(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already exists");
        }

        if (instituteTeacherService.isUserIdExists(userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User with this ID already has an account");
        }

        // Create a new InstituteTeacher object
        InstituteTeacher instituteTeacher = new InstituteTeacher();
        instituteTeacher.setTeacherId(teacherId);
        instituteTeacher.setTeacher_Name(teacherName);
        instituteTeacher.setSubject(subject);
        instituteTeacher.setInstitute_Type(instituteType);
        instituteTeacher.setLocation(location);
        instituteTeacher.setPassword(password);
        instituteTeacher.setConfirm_Password(confirmPassword);
        instituteTeacher.setEmail(email);
        instituteTeacher.setPincode(pincode);
        instituteTeacher.setStatus(status);

        // Register the teacher and send OTP
        InstituteTeacher savedTeacher = instituteTeacherService.registerTeacherAndSendOTP(instituteTeacher, instituteId, userId);

        return ResponseEntity.ok(savedTeacher);
    }
    @PostMapping("/verifyOtp/{id}")
    public ResponseEntity<String> verifyOtp(
            @PathVariable Long id,
            @RequestParam String otp) {
        String result = instituteTeacherService.verifyOtp(id, otp);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/verified-teachers/{instituteId}")
    public ResponseEntity<List<InstituteTeacher>> getVerifiedTeachersByInstituteId(@PathVariable Long instituteId) {
        List<InstituteTeacher> verifiedTeachers = instituteTeacherService.getVerifiedTeachersByInstituteId(instituteId);
        return ResponseEntity.ok(verifiedTeachers);
    }


    @PostMapping("/update-status/{id}")
    public ResponseEntity<String> updateTeacherStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String status = request.get("status");
        String result = instituteTeacherService.updateTeacherStatus(id, status);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/accepted/{userid}")
    public ResponseEntity<List<InstituteTeacher>> getAcceptedTeachersByUserId(@PathVariable Long userid) {
        // Calling the service to fetch the data
        List<InstituteTeacher> teachers = instituteTeacherService.getAcceptedTeachersByUserId(userid);

        // Check if teachers list is empty
        if (teachers.isEmpty()) {
            return ResponseEntity.noContent().build();  // Return 204 No Content if no data found
        }

        // Return the data with 200 OK status
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstituteTeacher> getTeacherById(@PathVariable Long id) {
        InstituteTeacher teacher = instituteTeacherService.getTeacherById(id);
        return ResponseEntity.ok(teacher);
    }



//    @GetMapping("/pending")
//    public ResponseEntity<List<InstituteTeacher>> getPendingTeachers() {
//        List<InstituteTeacher> teachers = instituteTeacherService.getTeachersByStatus(InstituteTeacher.Status.PENDING);
//        return ResponseEntity.ok(teachers);
//    }



}