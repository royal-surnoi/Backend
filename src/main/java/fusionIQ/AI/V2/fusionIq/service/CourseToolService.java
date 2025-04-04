package fusionIQ.AI.V2.fusionIq.service;

import fusionIQ.AI.V2.fusionIq.data.Course;
import fusionIQ.AI.V2.fusionIq.data.CourseTools;
import fusionIQ.AI.V2.fusionIq.exception.ResourceNotFoundException;
import fusionIQ.AI.V2.fusionIq.repository.CourseRepo;
import fusionIQ.AI.V2.fusionIq.repository.CourseToolRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class CourseToolService {
    @Autowired
    CourseToolRepo courseToolRepo;

    @Autowired
    CourseRepo courseRepo;

    public CourseTools saveCourseImage(Long courseId, MultipartFile toolImage, String skillName, MultipartFile skillImage, String toolName,String coursePrequisites) throws IOException {


        Optional<Course> courseOptional = courseRepo.findById(courseId);
        if (!courseOptional.isPresent()) {
            throw new IllegalArgumentException("Course not found");
        }

        Course course = courseOptional.get();
        CourseTools newcourseTools = new CourseTools();
        newcourseTools.setCourse(course);
        newcourseTools.setToolImage(toolImage.getBytes());
        newcourseTools.setToolName(toolName);
        newcourseTools.setSkillImage(skillImage.getBytes());
        newcourseTools.setSkillName(skillName);
        newcourseTools.setCoursePrerequisites(coursePrequisites);

        return courseToolRepo.save(newcourseTools);
    }

    public List<CourseTools> findAllTools() {
        return courseToolRepo.findAll();
    }

    public List<CourseTools> findToolsByCourseId(Long courseId) {
        return courseToolRepo.findToolsByCourseId(courseId);
    }
    public CourseTools updateCourseTools(Long id, CourseTools courseToolsDetails) {
        CourseTools courseTools = courseToolRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CourseTools", "id", id));

        courseTools.setToolImage(courseToolsDetails.getToolImage());
        courseTools.setToolName(courseToolsDetails.getToolName());
        courseTools.setSkillName(courseToolsDetails.getSkillName());
        courseTools.setSkillImage(courseToolsDetails.getSkillImage());
        courseTools.setCourse(courseToolsDetails.getCourse());
        courseTools.setCoursePrerequisites(courseToolsDetails.getCoursePrerequisites());

        return courseToolRepo.save(courseTools);
    }

    public void deleteCourseToolsProject(Long id) {
    }

    public CourseTools patchCourseToolsByCourseId(long courseId, Map<String, Object> updates) {
        Optional<Course> optionalCourse = courseRepo.findById(courseId);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            Optional<CourseTools> optionalCourseTools = course.getCourseTools().stream()
                    .findFirst();
            if (optionalCourseTools.isPresent()) {
                CourseTools courseTools = optionalCourseTools.get();
                updates.forEach((key, value) -> {
                    switch (key) {
                        case "toolImage":
                            courseTools.setToolImage((byte[]) value);
                            break;
                        case "toolName":
                            courseTools.setToolName((String) value);
                            break;
                        case "skillName":
                            courseTools.setSkillName((String) value);
                            break;
                        case "skillImage":
                            courseTools.setSkillImage((byte[]) value);
                            break;
                        case "coursePrerequisites":
                            courseTools.setCoursePrerequisites((String) value);
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid field: " + key);
                    }
                });
                return courseToolRepo.save(courseTools);
            }
        }
        return null;
    }
    public CourseTools updateCourseTools(Long courseId,
                                         byte[] toolImage,
                                         String toolName,
                                         String skillName,
                                         byte[] skillImage,
                                         String coursePrerequisites
    ) {
        Optional<CourseTools> optionalCourseTools = courseToolRepo.findByCourseId(courseId);
        if (optionalCourseTools.isPresent()) {
            CourseTools existingCourseTools = optionalCourseTools.get();
            existingCourseTools.setToolImage(toolImage);
            existingCourseTools.setToolName(toolName);
            existingCourseTools.setSkillName(skillName);
            existingCourseTools.setSkillImage(skillImage);
            existingCourseTools.setCoursePrerequisites(coursePrerequisites);

            return courseToolRepo.save(existingCourseTools);
        } else {
            throw new RuntimeException("CourseTools not found for course ID: " + courseId);
        }
    }

    public CourseTools saveProjects(CourseTools existingCourseTools) {
        return courseToolRepo.save(existingCourseTools);
    }

    public Optional<CourseTools> getCourseToolsById(long id) {
        return courseToolRepo.findById(id);
    }

    public List<CourseTools> saveMultipleCourseTools(Long courseId, List<MultipartFile> toolImages, List<String> skillNames, List<MultipartFile> skillImages, List<String> toolNames, List<String> coursePrerequisitesList) throws IOException {
        Optional<Course> courseOptional = courseRepo.findById(courseId);
        if (!courseOptional.isPresent()) {
            throw new IllegalArgumentException("Course not found");
        }

        Course course = courseOptional.get();
        List<CourseTools> courseToolsList = new ArrayList<>();

        for (int i = 0; i < toolNames.size(); i++) {
            CourseTools newCourseTools = new CourseTools();
            newCourseTools.setCourse(course);
            newCourseTools.setToolImage(toolImages.get(i).getBytes());
            newCourseTools.setToolName(toolNames.get(i));
            newCourseTools.setSkillImage(skillImages.get(i).getBytes());
            newCourseTools.setSkillName(skillNames.get(i));
            newCourseTools.setCoursePrerequisites(coursePrerequisitesList.get(i));
            courseToolsList.add(newCourseTools);
        }

        return courseToolRepo.saveAll(courseToolsList);
    }

    public List<CourseTools> saveMultipleCourseToolsV2(
            Long courseId,
            List<MultipartFile> toolImages,
            List<String> skillNames,
            List<MultipartFile> skillImages,
            List<String> toolNames,
            List<String> coursePrerequisitesList) throws IOException {

        Optional<Course> courseOptional = courseRepo.findById(courseId);
        if (!courseOptional.isPresent()) {
            throw new IllegalArgumentException("Course not found");
        }

        Course course = courseOptional.get();
        List<CourseTools> courseToolsList = new ArrayList<>();

        int maxLength = Math.max(Math.max(toolNames.size(), skillNames.size()),
                Math.max(toolImages.size(), coursePrerequisitesList.size()));

        for (int i = 0; i < maxLength; i++) {
            CourseTools newCourseTools = new CourseTools();
            newCourseTools.setCourse(course);

            // Handle tool information
            if (i < toolNames.size()) {
                newCourseTools.setToolName(toolNames.get(i));
                newCourseTools.setToolImage(i < toolImages.size() ? toolImages.get(i).getBytes() : null);
            }

            // Handle skill information
            if (i < skillNames.size()) {
                newCourseTools.setSkillName(skillNames.get(i));
                newCourseTools.setSkillImage(i < skillImages.size() ? skillImages.get(i).getBytes() : null);
            }

            // Handle course prerequisites
            if (i < coursePrerequisitesList.size()) {
                newCourseTools.setCoursePrerequisites(coursePrerequisitesList.get(i));
            }

            courseToolsList.add(newCourseTools);
        }

        return courseToolRepo.saveAll(courseToolsList);
    }

}
