package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.VideoLike;
import fusionIQ.AI.V2.fusionIq.service.VideoLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/videoLikes")
public class VideoLikeController {

    @Autowired
    private VideoLikeService videoLikeService;

    @GetMapping("/getAll")
    public List<VideoLike> getAllVideoLikes() {
        return videoLikeService.getAllVideoLikes();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<VideoLike> getVideoLikeById(@PathVariable long id) {
        return videoLikeService.getVideoLikeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public VideoLike createVideoLike(@RequestBody VideoLike videoLike) {
        return videoLikeService.createVideoLike(videoLike);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VideoLike> updateVideoLike(@PathVariable long id, @RequestBody VideoLike videoLikeDetails) {
        return ResponseEntity.ok(videoLikeService.updateVideoLike(id, videoLikeDetails));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVideoLike(@PathVariable long id) {
        videoLikeService.deleteVideoLike(id);
        return ResponseEntity.noContent().build();
    }
}

