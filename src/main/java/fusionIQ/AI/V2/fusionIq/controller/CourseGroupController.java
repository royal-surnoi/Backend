package fusionIQ.AI.V2.fusionIq.controller;

import fusionIQ.AI.V2.fusionIq.data.CourseGroup;
import fusionIQ.AI.V2.fusionIq.repository.UserRepo;
import fusionIQ.AI.V2.fusionIq.service.CourseGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CourseGroupController {

    @Autowired
    private CourseGroupService courseGroupService;

    @Autowired
    UserRepo userRepo;




    @GetMapping("/by-teacher/{teacherId}")
    public ResponseEntity<List<CourseGroup>> getCourseGroupsByTeacherId(@PathVariable Long teacherId) {
        List<CourseGroup> courseGroups = courseGroupService.getCourseGroupsByTeacherId(teacherId);
        return ResponseEntity.ok(courseGroups);
    }

    @PutMapping("/project-group/{id}")
    public ResponseEntity<CourseGroup> updateCourseGroup(
            @PathVariable Long id,
            @RequestBody CourseGroup updatedCourseGroup) {
        Optional<CourseGroup> updated = courseGroupService.updateCourseGroup(id, updatedCourseGroup);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/project-group/{id}")
    public ResponseEntity<Void> deleteCourseGroup(@PathVariable Long id) {
        courseGroupService.deleteCourseGroup(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/project-group/by-groupName/{groupName}")
    public ResponseEntity<Void> deleteCourseGroupByGroupName(@PathVariable String groupName) {
        courseGroupService.deleteCourseGroupByGroupName(groupName);
        return ResponseEntity.noContent().build();
    }

}
