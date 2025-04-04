package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.JobCommunity;
import fusionIQ.AI.V2.fusionIq.service.JobCommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/jobCommunity")
public class JobCommunityController {

    @Autowired
    private JobCommunityService jobCommunityService;

    // Endpoint to create a new community
    @PostMapping("/create/{userId}")
    public ResponseEntity<JobCommunity> createJobCommunity(
            @PathVariable Long userId,
            @RequestParam("communityName") String communityName,
            @RequestParam("communityDescription") String communityDescription,
            @RequestPart(value = "communityImage", required = false) MultipartFile communityImage) {
        try {
            // Create a JobCommunity object and set its fields
            JobCommunity jobCommunity = new JobCommunity();
            jobCommunity.setCommunityName(communityName);
            jobCommunity.setCommunityDescription(communityDescription);

            // Call the service to create the JobCommunity
            JobCommunity createdCommunity = jobCommunityService.createJobCommunity(userId, jobCommunity, communityImage);

            return new ResponseEntity<>(createdCommunity, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to add a user to an existing community
    @PostMapping("/addUser/{communityId}/{userId}")
    public ResponseEntity<JobCommunity> addUserToCommunity(
            @PathVariable Long communityId,
            @PathVariable Long userId) {
        JobCommunity updatedCommunity = jobCommunityService.addUserToCommunity(communityId, userId);
        return new ResponseEntity<>(updatedCommunity, HttpStatus.OK);
    }

//    @GetMapping("/all")
//    public ResponseEntity<List<JobCommunity>> getAllJobCommunities() {
//        List<JobCommunity> jobCommunities = jobCommunityService.getAllJobCommunities();
//        return new ResponseEntity<>(jobCommunities, HttpStatus.OK);
//    }

    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllJobCommunities() {
        List<Map<String, Object>> communities = jobCommunityService.getAllJobCommunities();
        return ResponseEntity.ok(communities);
    }
}
