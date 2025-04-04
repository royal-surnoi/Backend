package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.Job;
import fusionIQ.AI.V2.fusionIq.data.SavedJobs;
import fusionIQ.AI.V2.fusionIq.service.SavedJobsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/savedJobs")
public class SavedJobsController {

    @Autowired
    private SavedJobsService savedJobsService;

    @PostMapping("/save/{userId}/{jobId}")
    public ResponseEntity<SavedJobs> saveJob(@PathVariable Long userId, @PathVariable Long jobId) {
        SavedJobs saved = savedJobsService.saveJob(userId, jobId);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/getUser/{userId}")
    public ResponseEntity<List<SavedJobs>> getSavedJobsByUserId(@PathVariable Long userId) {
        List<SavedJobs> savedJobs = savedJobsService.getSavedJobsByUserId(userId);
        return ResponseEntity.ok(savedJobs);
    }
//@GetMapping("/user/{userId}")
//public ResponseEntity<List<Job>> getSavedJobsByUserId(@PathVariable Long userId) {
//    List<Job> savedJobs = savedJobsService.getSavedJobsByUserId(userId);
//    return ResponseEntity.ok(savedJobs);
//}

    @DeleteMapping("/unsave/{jobId}/{userId}")
    public String unsaveJob(@PathVariable Long jobId, @PathVariable Long userId) {
        savedJobsService.unsaveJob(jobId,userId);
        return "Job with ID " + jobId + " has been unsaved.";
    }
}