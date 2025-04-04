package fusionIQ.AI.V2.fusionIq.controller;


import fusionIQ.AI.V2.fusionIq.data.CourseTools;
import fusionIQ.AI.V2.fusionIq.repository.CourseToolRepo;
import fusionIQ.AI.V2.fusionIq.service.CourseToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/courseTools")
public class CourseToolController {
    @Autowired
    private CourseToolRepo courseToolRepo;

    @Autowired
    private CourseToolService courseToolService;

    @PostMapping("/saveCourseTool/{courseId}")

    public ResponseEntity<CourseTools> saveCourseImage(@PathVariable Long courseId,
                                                       @RequestParam("file") MultipartFile toolImage,
                                                       @RequestParam("file2") MultipartFile skillImage,
                                                       @RequestParam String toolName,
                                                       @RequestParam String skillName,
                                                       @RequestParam String coursePrerequisites
    ) throws IOException {
        CourseTools newCourseTools = courseToolService.saveCourseImage(courseId,toolImage,skillName, skillImage,toolName,coursePrerequisites);
        return new ResponseEntity<>(newCourseTools, HttpStatus.CREATED);
    }
    @GetMapping("/allTools")
    public ResponseEntity<List<CourseTools>> getAllTools() {
        List<CourseTools> courseTools = courseToolService.findAllTools();
        return ResponseEntity.ok(courseTools);
    }
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<CourseTools>> getCourseToolsByCourse(@PathVariable Long courseId) {
        List<CourseTools> courseTools = courseToolService.findToolsByCourseId(courseId);
        return ResponseEntity.ok(courseTools);
    }
    @PutMapping("/updateTool/{id}")
    public ResponseEntity<CourseTools> updateCourseTools(@PathVariable(value = "id") Long id,
                                                         @RequestBody CourseTools courseToolsDetails) {
        CourseTools updatedCourseTools = courseToolService.updateCourseTools(id, courseToolsDetails);
        return ResponseEntity.ok(updatedCourseTools);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCourseToolsProject(@PathVariable Long id) {
        courseToolService.deleteCourseToolsProject(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/patchCourseTools/{courseId}")
    public ResponseEntity<CourseTools> patchCourseToolsByCourseId(
            @PathVariable long courseId,
            @RequestBody Map<String, Object> updates) {

        CourseTools updatedCourseTools = courseToolService.patchCourseToolsByCourseId(courseId, updates);

        if (updatedCourseTools != null) {
            return ResponseEntity.ok(updatedCourseTools);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/updateCourseTools/{courseId}")
    public ResponseEntity<CourseTools> updateCourseTools(
            @PathVariable long courseId,
            @RequestParam(required = false) MultipartFile toolImage,
            @RequestParam(required = false) String toolName,
            @RequestParam(required = false) String skillName,
            @RequestParam(required = false) MultipartFile skillImage,
            @RequestParam(required = false) String coursePrerequisites
    ) {

        Optional<CourseTools> existingCourseToolsOptional = courseToolService.getCourseToolsById(courseId);
        if (existingCourseToolsOptional.isPresent()) {
            CourseTools existingCourseTools = existingCourseToolsOptional.get();

            if (toolImage != null && !toolImage.isEmpty()) {
                try {
                    byte[] documentBytes = toolImage.getBytes();
                    existingCourseTools.setToolImage(documentBytes);
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }

            if (toolName != null) {
                existingCourseTools.setToolName(toolName);
            }

            if (skillName != null) {
                existingCourseTools.setSkillName(skillName);
            }

            if (skillImage != null && !skillImage.isEmpty()) {
                try {
                    byte[] documentBytes = skillImage.getBytes();
                    existingCourseTools.setSkillImage(documentBytes);
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            }
            if (coursePrerequisites != null) {
                existingCourseTools.setCoursePrerequisites(coursePrerequisites);
            }

            CourseTools savedProject = courseToolService.saveProjects(existingCourseTools);
            return ResponseEntity.ok(savedProject);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/saveMultipleCourseTools/{courseId}")
    public ResponseEntity<List<CourseTools>> saveMultipleCourseTools(
            @PathVariable Long courseId,
            @RequestParam("toolImages") List<MultipartFile> toolImages,
            @RequestParam("skillImages") List<MultipartFile> skillImages,
            @RequestParam("toolNames") List<String> toolNames,
            @RequestParam("skillNames") List<String> skillNames,
            @RequestParam("coursePrerequisitesList") List<String> coursePrerequisitesList
    ) throws IOException {
        List<CourseTools> courseToolsList = courseToolService.saveMultipleCourseTools(courseId, toolImages, skillNames, skillImages, toolNames, coursePrerequisitesList);
        return new ResponseEntity<>(courseToolsList, HttpStatus.CREATED);
    }
    @PostMapping("/saveMultipleCourseTools2/{courseId}")
    public ResponseEntity<List<CourseTools>> saveMultipleCourseToolsV2(
            @PathVariable Long courseId,
            @RequestParam("toolImages") List<MultipartFile> toolImages,
            @RequestParam("skillImages") List<MultipartFile> skillImages,
            @RequestParam("toolNames") List<String> toolNames,
            @RequestParam("skillNames") List<String> skillNames,
            @RequestParam("coursePrerequisitesList") List<String> coursePrerequisitesList
    ) throws IOException {
        List<CourseTools> courseToolsList = courseToolService.saveMultipleCourseToolsV2(
                courseId, toolImages, skillNames, skillImages, toolNames, coursePrerequisitesList);
        return new ResponseEntity<>(courseToolsList, HttpStatus.CREATED);
    }




}
