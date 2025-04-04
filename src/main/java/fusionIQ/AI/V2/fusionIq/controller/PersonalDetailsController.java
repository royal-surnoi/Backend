package fusionIQ.AI.V2.fusionIq.controller;


import fusionIQ.AI.V2.fusionIq.data.Location;
import fusionIQ.AI.V2.fusionIq.data.PersonalDetails;
import fusionIQ.AI.V2.fusionIq.exception.UserNotFoundException;
import fusionIQ.AI.V2.fusionIq.service.PersonalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/personalDetails")
public class PersonalDetailsController {

    @Autowired
    private PersonalDetailsService personalDetailsService;

    @PostMapping("/create/{userId}")
    public ResponseEntity<PersonalDetails> createPersonalDetails(@PathVariable Long userId, @RequestBody PersonalDetails personalDetails) {
        PersonalDetails createdDetails = personalDetailsService.savePersonalDetails(userId, personalDetails);
        return new ResponseEntity<>(createdDetails, HttpStatus.CREATED);
    }

    @PostMapping("/createBy/{userId}")
    public ResponseEntity<PersonalDetails> createPersonalDetails(
            @PathVariable Long userId,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String userDescription,
            @RequestParam String userLanguage,
            @RequestParam String profession,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOfBirth,
            @RequestParam String phoneNumber,
            @RequestParam String permanentAddress,
            @RequestParam String permanentCity,
            @RequestParam String permanentState,
            @RequestParam String permanentCountry,
            @RequestParam String permanentZipcode,
            @RequestParam String interests,
            @RequestParam Double longitude,
            @RequestParam Double latitude,
            @RequestParam String skills,
            @RequestParam String gender,
            @RequestParam Integer age,
            @RequestParam boolean isProfileComplete,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAt,
            @RequestParam PersonalDetails.Theme theme,
            @RequestParam String selfIntroductionVideo,
            @RequestParam String s3Key,
            @RequestParam String s3Url,
            @RequestParam("resume") MultipartFile resume,
            @RequestParam("bannerImage") MultipartFile bannerImage) throws IOException {

        // Convert files to byte arrays
        byte[] resumeBytes = resume.getBytes();
        byte[] bannerImageBytes = bannerImage.getBytes();

        // Call service method to save personal details
        PersonalDetails createdDetails = personalDetailsService.savePersonalDetails(
                userId,
                firstName,
                lastName,
                userDescription,
                userLanguage,
                profession,
                dateOfBirth,
                phoneNumber,
                permanentAddress,
                permanentCity,
                permanentState,
                permanentCountry,
                permanentZipcode,
                interests,
                longitude,
                latitude,
                skills,
                gender,
                age,
                isProfileComplete,
                createdAt,
                theme,
                selfIntroductionVideo,
                s3Key,
                s3Url,
                resumeBytes,
                bannerImageBytes);

        return ResponseEntity.ok(createdDetails);
    }

    @GetMapping("/get/user/{userId}")
    public ResponseEntity<PersonalDetails> getPersonalDetailsByUserId(@PathVariable("userId") Long userId) {
        PersonalDetails details = personalDetailsService.getPersonalDetailsByUserId(userId);
        if (details != null) {
            return new ResponseEntity<>(details, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PersonalDetails> getPersonalDetailsById(@PathVariable("id") Long id) {
        PersonalDetails details = personalDetailsService.getPersonalDetailsById(id);
        if (details != null) {
            return new ResponseEntity<>(details, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<PersonalDetails>> getAllPersonalDetails() {
        List<PersonalDetails> detailsList = personalDetailsService.getAllPersonalDetails();
        return new ResponseEntity<>(detailsList, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePersonalDetails(@PathVariable("id") Long id) {
        personalDetailsService.deletePersonalDetails(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/ /{id}")
    public ResponseEntity<PersonalDetails> updatePersonalDetails(@PathVariable Long id, @RequestBody PersonalDetails updatedDetails) {
        try {
            PersonalDetails updatedPersonalDetails = personalDetailsService.updatePersonalDetails(id, updatedDetails);
            return new ResponseEntity<>(updatedPersonalDetails, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/location")
    public ResponseEntity<Void> updateLocation(
            @RequestParam Long userId,
            @RequestBody Location location) {
        try {
            personalDetailsService.updateLocation(userId, location.getLatitude(), location.getLongitude());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/checkAndSetProfileComplete/{userId}")
    public ResponseEntity<PersonalDetails> getPersonalDetails(@PathVariable Long userId) {
        PersonalDetails details = personalDetailsService.findByUserId(userId);
        if (details != null) {
            return ResponseEntity.ok(details);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{userId}/uploadResume")
    public ResponseEntity<String> uploadResume(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        try {
            PersonalDetails updatedDetails = personalDetailsService.uploadResume(userId, file);
            return new ResponseEntity<>("Resume uploaded successfully.", HttpStatus.OK);
        } catch (IOException | UserNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Resume upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/update-theme/{userId}")
    public ResponseEntity<PersonalDetails> updateTheme(
            @PathVariable Long userId,
            @RequestParam PersonalDetails.Theme theme) {

        PersonalDetails updatedDetails = personalDetailsService.updateTheme(userId, theme);
        return new ResponseEntity<>(updatedDetails, HttpStatus.OK);
    }

    // API to update the banner image
    @PutMapping("/update-banner/{userId}")
    public ResponseEntity<PersonalDetails> updateBannerImage(
            @PathVariable Long userId,
            @RequestParam("bannerImage") MultipartFile bannerImage) throws IOException {

        PersonalDetails updatedDetails = personalDetailsService.updateBannerImage(userId, bannerImage);
        return new ResponseEntity<>(updatedDetails, HttpStatus.OK);
    }
    @GetMapping("/{userId}/getResume")
    public ResponseEntity<byte[]> getResume(@PathVariable Long userId) {
        try {
            byte[] resumeData = personalDetailsService.getResumeByUserId(userId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=resume.pdf"); // Set appropriate file name and type

            return new ResponseEntity<>(resumeData, headers, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updatePersonalDetails/{id}")
    public ResponseEntity<?> updateSomePersonalDetails(
            @PathVariable Long id,
            @RequestBody PersonalDetails updatedDetails) {
        try {
            boolean isUpdated = personalDetailsService.updateSomePersonalDetails(id, updatedDetails);
            if (isUpdated) {
                return ResponseEntity.ok("Personal details updated successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Personal details with ID " + id + " not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }

    }
    @GetMapping("/allDetailsForAIResumeBuilder/{userId}")
    public Map<String, Object> getUserProfile(@PathVariable Long userId) {
        return personalDetailsService.getUserProfile(userId);
    }

}
