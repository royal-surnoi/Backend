package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.InstituteStudent;
import fusionIQ.AI.V2.fusionIq.service.InstituteStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class InstituteStudentController {

    @Autowired
    private InstituteStudentService instituteStudentService;

    // Register a new student with OTP generation and sending via email
    // Register a new student with OTP generation and sending via email
    @PostMapping("/register/{instituteId}/{userId}")
    public ResponseEntity<?> registerStudent(
            @PathVariable long instituteId,
            @PathVariable long userId,
            @RequestBody InstituteStudent student) {

        try {
            // Register the student and send OTP via email
            InstituteStudent savedStudent = instituteStudentService.registerStudentWithOtp(instituteId, userId, student);
            return ResponseEntity.ok(savedStudent);
        } catch (RuntimeException e) {
            // Return a bad request response with the error message
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify-otp/{id}")
    public ResponseEntity<String> verifyOtp(@PathVariable Long id, @RequestParam String otp) {
        String response = instituteStudentService.verifyOtp(id, otp);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/accepted/{userid}")
    public ResponseEntity<List<InstituteStudent>> getAcceptedStudentsByUserId(@PathVariable Long userid) {
        List<InstituteStudent> students = instituteStudentService.getAcceptedStudentsByUserId(userid);
        return ResponseEntity.ok(students);
    }
    @GetMapping("/{id}")
    public ResponseEntity<InstituteStudent> getStudentById(@PathVariable Long id) {
        InstituteStudent student = instituteStudentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }


    @GetMapping("/verified/{instituteId}")
    public ResponseEntity<List<InstituteStudent>> getVerifiedStudents(@PathVariable Long instituteId) {
        List<InstituteStudent> students = instituteStudentService.getVerifiedStudentsByInstituteId(instituteId);
        return ResponseEntity.ok(students);
    }

    @PostMapping("/update-status/{id}")
    public ResponseEntity<String> updateStudentStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String status = request.get("status");
        String result = instituteStudentService.updateStudentStatus(id, status);
        return ResponseEntity.ok(result);
    }


}