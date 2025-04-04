package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.SelfIntroductionVideos;
import fusionIQ.AI.V2.fusionIq.repository.SelfIntroductionVideosRepo;
import fusionIQ.AI.V2.fusionIq.service.SelfIntroductionVideosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

@RestController
@RequestMapping("/selfIntroductionVideos")
public class SelfIntroductionVideosController {

    @Autowired
    private SelfIntroductionVideosService selfIntroductionVideosService;
    public SelfIntroductionVideosController(SelfIntroductionVideosService selfIntroductionVideosService) {
        this.selfIntroductionVideosService = selfIntroductionVideosService;
    }

    @Autowired
    private SelfIntroductionVideosRepo selfIntroductionVideosRepo;

    @PostMapping("/{userId}/upload")
    public ResponseEntity<String> uploadSelfIntroductionVideos(
            @PathVariable Long userId,
            @RequestParam("personalVideo") MultipartFile personalVideo,
            @RequestParam("educationVideo") MultipartFile educationVideo,
            @RequestParam("workExperienceVideo") MultipartFile workExperienceVideo,
            @RequestParam("achievementsVideo") MultipartFile achievementsVideo) {
        try {
            selfIntroductionVideosService.uploadAllVideos(userId, personalVideo, educationVideo, workExperienceVideo, achievementsVideo);
            return new ResponseEntity<>("Videos uploaded successfully.", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Video upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<SelfIntroductionVideos> getSelfIntroductionVideosByUserId(@PathVariable Long userId) {
        // Assuming you have a method in your SelfIntroductionVideosRepo to find videos by user ID
        SelfIntroductionVideos videos = selfIntroductionVideosRepo.findByUserId(userId);

        if (videos != null) {
            return new ResponseEntity<>(videos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/{userId}/createTimestamps")
    public ResponseEntity<String> createOrUpdateTimestamps(
            @PathVariable Long userId,
            @RequestBody Map<String, BigInteger> timestamps) {
        try {
            selfIntroductionVideosService.createOrUpdateTimestampsByUserId(userId, timestamps);
            return ResponseEntity.ok("Timestamps created or updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating or updating timestamps: " + e.getMessage());
        }
    }

    @GetMapping("/combined/{userId}")
    public ResponseEntity<Map<String, Object>> getSelfIntroductionVideoByUserId(@PathVariable Long userId) {
        try {
            Map<String, Object> response = selfIntroductionVideosService.getSelfIntroductionVideosByUserId(userId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{userId}/updateCombined")
    public ResponseEntity<String> updateCombinedSelfIntroductionVideo(
            @PathVariable Long userId,
            @RequestParam("combinedVideo") MultipartFile combinedVideo) {
        try {
            selfIntroductionVideosService.uploadCombinedVideo(userId, combinedVideo);
            return new ResponseEntity<>("Combined video updated successfully.", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Combined video update failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }}

    @PutMapping("/videos/{userId}")
    public ResponseEntity<String> updateSelfIntroductionVideos(
            @PathVariable Long userId,
            @RequestParam(required = false) MultipartFile personalVideo,
            @RequestParam(required = false) MultipartFile educationVideo,
            @RequestParam(required = false) MultipartFile workExperienceVideo,
            @RequestParam(required = false) MultipartFile achievementsVideo) throws IOException {

        // Fetch the user and videos entity
        try {
            selfIntroductionVideosService.updateVideos(userId, personalVideo, educationVideo, workExperienceVideo, achievementsVideo);
            return new ResponseEntity<>("Videos uploaded successfully.", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Video upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
