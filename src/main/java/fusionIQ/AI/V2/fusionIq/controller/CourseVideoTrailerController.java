package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.CourseVideoTrailer;
import fusionIQ.AI.V2.fusionIq.repository.CourseVideoTrailerRepo;
import fusionIQ.AI.V2.fusionIq.service.CourseVideoTrailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/course")
public class CourseVideoTrailerController {

    @Autowired
    CourseVideoTrailerService courseVideoTrailerService;

    @Autowired
    CourseVideoTrailerRepo courseVideoTrailerRepo;

    @PostMapping("/uploadTrailer/{courseId}")
    public ResponseEntity<Map<String, String>> uploadVideoTrailer(
            @PathVariable Long courseId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String videoTrailerTitle,
            @RequestParam("description") String videoTrailerDescription) {
        try {
            CourseVideoTrailer videoTrailer = courseVideoTrailerService.uploadTrailer(file, courseId, videoTrailerTitle, videoTrailerDescription);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Video trailer uploaded successfully");
            response.put("url", videoTrailer.getS3Url());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Video trailer upload failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getCourseTrailer/{courseId}")
    public ResponseEntity<List<CourseVideoTrailer>> getVideoTrailersByCourseId(@PathVariable Long courseId) {
        List<CourseVideoTrailer> videoTrailers = courseVideoTrailerService.getVideoTrailersByCourseId(courseId);
        if (videoTrailers.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(videoTrailers);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideoTrailerById(@PathVariable Long id) {
        try {
            courseVideoTrailerService.deleteVideoTrailerById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
