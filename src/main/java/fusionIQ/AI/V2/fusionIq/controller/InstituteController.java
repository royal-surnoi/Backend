package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.*;
import fusionIQ.AI.V2.fusionIq.service.InstituteService;
import fusionIQ.AI.V2.fusionIq.service.InstituteStudentService;
import fusionIQ.AI.V2.fusionIq.service.InstituteTeacherService;
import fusionIQ.AI.V2.fusionIq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/institute")
public class InstituteController {

    @Autowired
    private InstituteService instituteService;

    @Autowired
    private InstituteTeacherService instituteTeacherService;

    @Autowired
    private InstituteStudentService instituteStudentService;

    @Autowired
    private UserService userService;

    // POST API to create an Organisation

    @PostMapping("/register")
    public ResponseEntity<String> registerInstitute(
            @RequestParam("institute_Name") String instituteName,
            @RequestParam("location") String location,
            @RequestParam("principal_name") String principalName,
            @RequestParam("institute_Registration_No") String registrationNo,
            @RequestParam("instituteType") String instituteType,
            @RequestParam("board") String board,
            @RequestParam("email") String email,
            @RequestParam("addPassword") String addPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam("pincode") String pincode,
            @RequestParam("userId") Long userId, // User ID for association
            @RequestPart("image") MultipartFile image) {

        // Call the service method to register the institute
        Institute institute = instituteService.registerInstitute(
                instituteName, location, principalName, registrationNo, instituteType, board, email,
                addPassword, confirmPassword, pincode, userId, image);

        return ResponseEntity.ok("Institute registered successfully with ID: " + institute.getId());
    }

    // GET API to retrieve all Institutes
    @GetMapping("/all")
    public ResponseEntity<List<Institute>> getAllInstitutes() {
        List<Institute> institutes = instituteService.getAllinstitutes();
        return ResponseEntity.ok(institutes);
    }

    // PUT API to update teacher status by organisation
    @PutMapping("/{instituteId}/teachers/{teacherId}/status")
    public ResponseEntity<String> updateTeacherStatus(
            @PathVariable Long instituteId,
            @PathVariable String teacherId,
            @RequestParam("status") InstituteTeacher.Status status) {

        try {
            boolean isUpdated = instituteService.updateTeacherStatusByInstitute(instituteId, teacherId, status);

            if (isUpdated) {
                return ResponseEntity.ok("Teacher status updated successfully.");
            } else {
                return ResponseEntity.badRequest().body("Teacher not found for the given institute or invalid details provided.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while updating teacher status: " + e.getMessage());
        }
    }


    // PUT API to update student status by organisation
    @PutMapping("/{instituteId}/students/{studentId}/status")
    public ResponseEntity<String> updateStudentStatus(
            @PathVariable Long instituteId,
            @PathVariable Long studentId,
            @RequestParam("status") InstituteStudent.Status status) {

        boolean isUpdated = instituteService.updateStudentStatusByinstitute(instituteId, studentId, status);

        if (isUpdated) {
            return ResponseEntity.ok("Student status updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to update student status. Please check the provided details.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Institute> getinstituteById(@PathVariable Long id) {
        Institute institute = instituteService.getinstituteById(id);
        return ResponseEntity.ok(institute);
    }
    @GetMapping("/institutes/{userId}")
    public ResponseEntity<List<Institute>> getInstitutesByUserId(@PathVariable Long userId) {
        List<Institute> institutes = instituteService.getInstitutesByUserId(userId);
        return ResponseEntity.ok(institutes);
    }



    // Update Organisation details (second form data)
    @PutMapping("/{id}/details")
    public ResponseEntity<Institute> updateInstituteDetails(
            @PathVariable Long id,
            @RequestParam("contactNo") String contactNo,
            @RequestParam("description") String description,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam("establishedIn") int establishedIn) {

        byte[] profileImageBytes = null;

        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                profileImageBytes = profileImage.getBytes();
            } catch (IOException e) {
                throw new RuntimeException("Failed to read profile image", e);
            }
        }

        Institute updatedInstitute = instituteService.updateInstituteDetails(id, contactNo, description, profileImageBytes, establishedIn);
        return ResponseEntity.ok(updatedInstitute);
    }

    @GetMapping("/search")
    public List<Institute> getInstitutesByCriteria(
            @RequestParam(required = false) String pincode,
            @RequestParam(required = false) String institute_Name,
            @RequestParam(required = false) String instituteType,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer establishedIn) {

        return instituteService.getInstitutesByCriteria(pincode, institute_Name, instituteType, location, establishedIn);
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateInstitute(
            @RequestParam("id") Long id,
            @RequestParam("institute_Name") String instituteName,
            @RequestParam("location") String location,
            @RequestParam("principal_name") String principalName,
            @RequestParam("institute_Registration_No") String registrationNo,
            @RequestParam("instituteType") String instituteType,
            @RequestParam("board") String board,
            @RequestParam("email") String email,
            @RequestParam("addPassword") String addPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam("pincode") String pincode,
            @RequestParam("contactNo") String contactNo,
            @RequestParam("description") String description,
            @RequestParam("establishedIn") int establishedIn,
            @RequestPart("profileImage") MultipartFile profileImage) {

        // Call the service method to update the institute
        Institute updatedInstitute = instituteService.updateInstitute(
                id, instituteName, location, principalName, registrationNo, instituteType, board,
                email, addPassword, confirmPassword, pincode, contactNo, description, establishedIn, profileImage);

        return ResponseEntity.ok("Institute updated successfully with ID: " + updatedInstitute.getId());
    }

//    @GetMapping("/accountdetails/{userId}")
//    public ResponseEntity<Map<String, Object>> getAccountDetailsByUserId(@PathVariable Long userId) {
//        // Fetch institutes associated with the user
//        List<Institute> institutes = instituteService.getInstitutesByUserId(userId);
//
//        // Fetch accepted teachers associated with the user
//        List<InstituteTeacher> acceptedTeachers = instituteTeacherService.getAcceptedTeachersByUserId(userId);
//
//        // Fetch accepted students associated with the user
//        List<InstituteStudent> acceptedStudents = instituteStudentService.getAcceptedStudentsByUserId(userId);
//
//        // Combine all the fetched data into a single map
//        Map<String, Object> response = new HashMap<>();
//        response.put("institutes", institutes);
//        response.put("acceptedTeachers", acceptedTeachers);
//        response.put("acceptedStudents", acceptedStudents);
//
//        // Return the response as JSON
//        return ResponseEntity.ok(response);
//    }



    // Get data by InstituteStudent ID
    @GetMapping("/student/{id}")
    public ResponseEntity<?> getByInstituteStudentId(@PathVariable long id) {
        return ResponseEntity.ok(instituteService.getByInstituteStudentId(id));
    }

    // Get data by InstituteTeacher ID
    @GetMapping("/teacher/{id}")
    public ResponseEntity<?> getByInstituteTeacherId(@PathVariable long id) {
        return ResponseEntity.ok(instituteService.getByInstituteTeacherId(id));
    }

    @GetMapping("/GetAccounts/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getRolesWithInstitutesByUserId(
            @PathVariable Long userId) {
        List<Map<String, Object>> roles = instituteService.getAllRolesWithInstitutesByUserId(userId);
        return ResponseEntity.ok(roles);
    }
}
