package fusionIQ.AI.V2.fusionIq.controller;


import fusionIQ.AI.V2.fusionIq.data.Search;
import fusionIQ.AI.V2.fusionIq.repository.SearchRepo;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private SearchRepo searchRepo;

    @Autowired
    private SearchService searchService;

    @Autowired
    UserRepo userRepo;


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Search>> getSearchesByUserId(@PathVariable Long userId) {
        List<Search> searches = searchRepo.findByUserId(userId);
        if (searches.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(searches);
    }




    @DeleteMapping("/deleteSearch/{id}")
    public ResponseEntity<Void> deleteSearch(@PathVariable Long id) {
        searchService.deleteSearch(id);
        return ResponseEntity.noContent().build();
    }

@PostMapping("/storeSearchContent")
public Search storeSearchContent(@RequestParam Long userId, @RequestParam String searchContent) {
    return searchService.storeSearchContent(userId, searchContent);
}

@PostMapping("/storeCourseContent")
public Search storeCourseContent(@RequestParam Long userId, @RequestParam String courseContent) {
    return searchService.storeCourseContent(userId, courseContent);
}

@GetMapping("/getSearchContent")
public List<Search> getSearchContent(@RequestParam Long userId) {
    return searchService.getSearchContentByUserId(userId);
}

@GetMapping("/getCourseContent")
public List<Search> getCourseContent(@RequestParam Long userId) {
    return searchService.getCourseContentByUserId(userId);
}
    @GetMapping("/all/getSearchContent")
    public List<Search> getSearchContent() {
        return searchService.getSearchContent();
    }

    @GetMapping("/all/getCourseContent")
    public List<Search> getCourseContent() {
        return searchService.getCourseContent();
    }
    @GetMapping("/recent-courses/{userId}")
    public ResponseEntity<List<String>> getLast10CourseContentForUser(@PathVariable Long userId) {
        List<String> courseContents = searchService.getLast10CourseContentByUser(userId);

        if (courseContents.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(courseContents);
    }

    @GetMapping("/recent-jobs/{userId}")
    public ResponseEntity<List<String>> getLast10JobContentForUser(@PathVariable Long userId) {
        List<String> jobContents = searchService.getLast10JobContentByUser(userId);

        if (jobContents.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(jobContents);
    }
}